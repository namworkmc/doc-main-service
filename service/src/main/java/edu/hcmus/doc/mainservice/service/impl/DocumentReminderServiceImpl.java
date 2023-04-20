package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.DocDateTime;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.util.mapper.DocumentReminderMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DocumentReminderServiceImpl implements DocumentReminderService {

  private final TaskScheduler taskScheduler;

  private final DocumentReminderRepository documentReminderRepository;

  private final UserRepository userRepository;

  private final DocumentReminderMapper documentReminderMapper;

  @Override
  public void scheduleReminder(ProcessingDocument processingDocument) {
//    Date startTime = Date.from(processingDocument
//        .getProcessingDuration()
//        .atStartOfDay(ZoneId.systemDefault())
//        .minusDays(DocDateTime.expirationDate)
//        .toInstant());

    // TODO: For testing
    Date startTime = Date.from(LocalDateTime.now()
        .atZone(ZoneId.systemDefault())
        .plusSeconds(10)
        .toInstant());

    taskScheduler.schedule(() -> {
          DocumentReminder documentReminder = new DocumentReminder();
          documentReminder.setProcessingDoc(processingDocument);
          documentReminder.setExecutionTime(LocalDateTime.now());
          documentReminder.setExpirationDate(LocalDate.now().plusDays(DocDateTime.EXPIRATION_DATE));
          documentReminderRepository.save(documentReminder);
        },
        startTime);
  }

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    return documentReminderRepository.getDocumentReminderStatusByUserIdAndTime(SecurityUtils.getCurrentUserId(), yearMonth);
  }

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year) {
    return documentReminderRepository.getDocumentReminderStatusByUserIdAndTime(SecurityUtils.getCurrentUserId(), year);
  }

  @Override
  public Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentRemindersDetailsByTime(LocalDate date) {
    return documentReminderRepository
        .getDocumentReminderDetailByUserIdAndTime(SecurityUtils.getCurrentUserId(), date)
        .entrySet()
        .stream()
        .map(entry -> {
          DocumentReminderStatusEnum status = entry.getKey();

          Set<DocumentReminderDetailsDto> details = entry.getValue()
              .stream()
              .map(processingDocument -> {
                DocumentReminderDetailsDto dto = new DocumentReminderDetailsDto();
                dto.setProcessingDocumentId(processingDocument.getId());
                dto.setVersion(processingDocument.getVersion());
                dto.setIncomingNumber(processingDocument.getIncomingDoc().getIncomingNumber());
                dto.setSummary(processingDocument.getIncomingDoc().getSummary());
                dto.setExpirationDate(date);
                return dto;
              })
              .collect(Collectors.toSet());

          return new AbstractMap.SimpleEntry<>(status, details);
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentRemindersDetailsByTime(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    return documentReminderRepository
        .getDocumentReminderDetailByUserIdAndTime(
            SecurityUtils.getCurrentUserId(),
            yearMonth)
        .entrySet()
        .stream()
        .map(entry -> {
          DocumentReminderStatusEnum status = entry.getKey();

          Set<Set<DocumentReminderDetailsDto>> details = entry.getValue()
              .entrySet()
              .stream()
              .map(processingEntry -> processingEntry.getValue()
                  .stream()
                  .map(processingDocument -> {
                    DocumentReminderDetailsDto dto = new DocumentReminderDetailsDto();
                    dto.setId(processingDocument.getId());
                    dto.setVersion(processingDocument.getVersion());
                    dto.setIncomingNumber(processingDocument.getIncomingDoc().getIncomingNumber());
                    dto.setSummary(processingDocument.getIncomingDoc().getSummary());
                    dto.setExpirationDate(processingEntry.getKey());
                    dto.setProcessingDocumentId(processingDocument.getId());
                    return dto;
                  })
                  .collect(Collectors.toSet()))
              .collect(Collectors.toSet());

          return new AbstractMap.SimpleEntry<>(status, details.stream().flatMap(Set::stream).collect(Collectors.toSet()));
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public long countUnreadDocumentReminders() {
    return documentReminderRepository.countByUserId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public long updateCurrentUserIsNotOpenedReminders(List<Long> ids) {
    List<DocumentReminder> reminders = documentReminderRepository.getDocumentRemindersInAndIsNotOpen(ids, SecurityUtils.getCurrentUserId());
    reminders.forEach(documentReminder -> documentReminder.setOpened(true));

    return documentReminderRepository.saveAll(reminders).size();
  }
}

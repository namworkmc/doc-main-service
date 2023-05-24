package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.service.ScheduleService;
import edu.hcmus.doc.mainservice.util.DocDateTimeUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DocumentReminderServiceImpl implements DocumentReminderService {

  private final ScheduleService scheduleService;

  private final DocumentReminderRepository documentReminderRepository;

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
                dto.setIncomingDocumentId(processingDocument.getIncomingDoc().getId());
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
                    dto.setIncomingDocumentId(processingDocument.getId());
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

  @Override
  public Long createdDocumentReminder(ProcessingUser processingUser) {
    DocumentReminder documentReminder = new DocumentReminder();
    documentReminder.setProcessingUser(processingUser);
    documentReminder.setExpirationDate(processingUser.getProcessingDuration());
    documentReminder.setStatus(DocumentReminderStatusEnum.ACTIVE);

    LocalDateTime setCloseToExpirationDate = DocDateTimeUtils.getAtStartOf7DaysBefore(processingUser.getProcessingDuration());
    log.info("Schedule change document reminder status {} run at: {}", DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION, setCloseToExpirationDate);
    scheduleService.changeDocumentReminderStatus(
        processingUser,
        LocalDateTime.now().plusMinutes(1),
        DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION
    );

    LocalDateTime setExpiredDate = DocDateTimeUtils.getAtEndOfDay(processingUser.getProcessingDuration());
    log.info("Schedule change document reminder status {} run at: {}", DocumentReminderStatusEnum.EXPIRED, setExpiredDate);
    scheduleService.changeDocumentReminderStatus(
        processingUser,
        LocalDateTime.now().plusMinutes(2),
        DocumentReminderStatusEnum.EXPIRED
    );
    return documentReminderRepository.save(documentReminder).getId();
  }
}

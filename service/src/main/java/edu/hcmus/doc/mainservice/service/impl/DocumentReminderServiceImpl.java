package edu.hcmus.doc.mainservice.service.impl;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.MobileNotificationMessageDto;
import edu.hcmus.doc.mainservice.model.entity.DocFirebaseTokenEntity;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocFirebaseTokenType;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.FirebaseTokenRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.util.DocDateTimeUtils;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DocumentReminderServiceImpl implements DocumentReminderService {

  private final UserRepository userRepository;

  private static final String FIREBASE_NOTIFICATION_RESPONSE_LOG = "Push notification response: {}";

  private final FirebaseMessaging firebaseMessaging;

  private final DocumentReminderRepository documentReminderRepository;

  private final FirebaseTokenRepository firebaseTokenRepository;

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
  public Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> getCurrentUserDocumentRemindersDetailsByTime(LocalDate date) {
    return documentReminderRepository.getDocumentReminderDetailByUserIdAndTime(SecurityUtils.getCurrentUserId(), date);
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
                    dto.setDocumentNumber(processingDocument.getIncomingDoc().getIncomingNumber());
                    dto.setSummary(processingDocument.getIncomingDoc().getSummary());
                    dto.setExpirationDate(processingEntry.getKey());
                    dto.setDocumentId(processingDocument.getId());
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
  public void createDocumentReminder(ProcessingUser processingUser) {
    DocumentReminder documentReminder = new DocumentReminder();
    documentReminder.setProcessingUser(processingUser);
    documentReminder.setExpirationDate(processingUser.getProcessingDuration());

    try {
      MobileNotificationMessageDto message;
      if (DocDateTimeUtils.isBetween(LocalDateTime.now(),
          DocDateTimeUtils.getAtStartOf7DaysBefore(processingUser.getProcessingDuration()),
          DocDateTimeUtils.getAtEndOfDay(processingUser.getProcessingDuration()))) {
        documentReminder.setStatus(DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION);
      } else if (DocDateTimeUtils.getAtEndOfDay(processingUser.getProcessingDuration()).isBefore(LocalDateTime.now())) {
        documentReminder.setStatus(DocumentReminderStatusEnum.EXPIRED);
      } else {
        documentReminder.setStatus(DocumentReminderStatusEnum.ACTIVE);
        documentReminderRepository.save(documentReminder);
        return;
      }

      if (processingUser.getProcessingDocument().getIncomingDoc() != null){
        message = buildMobileNotificationMessage(
            documentReminder.getStatus(),
            processingUser.getProcessingDocument().getIncomingDoc().getIncomingNumber());
        message.setProcessingDocumentType(ProcessingDocumentTypeEnum.INCOMING_DOCUMENT);
        message.setDocumentId(processingUser.getProcessingDocument().getIncomingDoc().getId());
      } else {
        message = buildMobileNotificationMessage(
            documentReminder.getStatus(),
            processingUser.getProcessingDocument().getOutgoingDocument().getOutgoingNumber());
        message.setProcessingDocumentType(ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT);
        message.setDocumentId(processingUser.getProcessingDocument().getOutgoingDocument().getId());
      }

      pushMobileNotificationsByUserId(message, processingUser.getUser().getId());
    } catch (FirebaseMessagingException e) {
      log.error("Error when sending mobile notification: {}", e.getMessage(), e);
    }

    documentReminderRepository.save(documentReminder);
  }

  @Override
  public void pushMobileNotification(MobileNotificationMessageDto mobileNotificationMessageDto)
      throws FirebaseMessagingException {
    Message message = Message.builder()
        .putData("processingDocumentType", mobileNotificationMessageDto.getProcessingDocumentType().name())
        .putData("documentId", mobileNotificationMessageDto.getDocumentId().toString())
        .setNotification(Notification.builder()
            .setTitle(mobileNotificationMessageDto.getTitle())
            .setBody(mobileNotificationMessageDto.getBody())
            .build())
        .setToken(mobileNotificationMessageDto.getToken())
        .build();

    String res = firebaseMessaging.send(message);
    log.info(FIREBASE_NOTIFICATION_RESPONSE_LOG, res);

  }

  @Override
  public void pushMobileNotificationsByUserId(
      MobileNotificationMessageDto mobileNotificationMessageDto, Long userId)
      throws FirebaseMessagingException {
    List<String> tokens = firebaseTokenRepository
        .getTokensByUserId(userId)
        .stream()
        .map(DocFirebaseTokenEntity::getToken)
        .toList();

    if (CollectionUtils.isEmpty(tokens)) {
      log.warn("No token found for user {}", userId);
      return;
    }

    MulticastMessage multicastMessage = MulticastMessage.builder()
        .putData("processingDocumentType", mobileNotificationMessageDto.getProcessingDocumentType().name())
        .setNotification(Notification.builder()
            .setTitle(mobileNotificationMessageDto.getTitle())
            .setBody(mobileNotificationMessageDto.getBody())
            .build())
        .addAllTokens(tokens)
        .build();
    BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage);
    batchResponse.getResponses().forEach(response -> Optional.ofNullable(response.getMessageId())
        .ifPresentOrElse(
            messageId -> log.info(FIREBASE_NOTIFICATION_RESPONSE_LOG, messageId),
            () -> log.error("Error when sending mobile notification: {}",
                response.getException().getMessage(), response.getException())));
  }

  @Override
  public MobileNotificationMessageDto buildMobileNotificationMessage(
      DocumentReminderStatusEnum documentReminderStatus,
      String documentNumber) {

    String title = null;
    String body = null;
    try {
      if (documentReminderStatus == DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION) {
        title = DocMessageUtils.getContent("reminder.message.close-to-expire.title");
        body = DocMessageUtils.getContent("reminder.message.close-to-expire.body", documentNumber);
      } else if (documentReminderStatus == DocumentReminderStatusEnum.EXPIRED) {
        title = DocMessageUtils.getContent("reminder.message.expired.title");
        body = DocMessageUtils.getContent("reminder.message.expired.body", documentNumber);
      }

      if (StringUtils.isAnyBlank(title, body)) {
        throw new IllegalArgumentException("Title: " + title + " or body " + body + " is blank");
      }
    } catch (IllegalArgumentException e) {
      log.error("Error when building mobile notification message: {}", e.getMessage(), e);
    }

    return MobileNotificationMessageDto.builder()
        .title(title)
        .body(body)
        .build();
  }

  @Override
  public void saveAll(List<DocumentReminder> documentReminders) {
    documentReminderRepository.saveAll(documentReminders);
  }

  @Override
  public List<DocumentReminder> getDocumentRemindersByStatusIn(
      List<DocumentReminderStatusEnum> statuses) {
    return documentReminderRepository.getDocumentRemindersByStatusIn(
        List.of(DocumentReminderStatusEnum.ACTIVE,
            DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION));
  }
}

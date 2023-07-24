package edu.hcmus.doc.mainservice.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.MobileNotificationMessageDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DocumentReminderService {

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year, int month);

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year);

  Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> getCurrentUserDocumentRemindersDetailsByTime(LocalDate date);

  Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentRemindersDetailsByTime(int year, int month);

  long countUnreadDocumentReminders();

  long updateCurrentUserIsNotOpenedReminders(List<Long> ids);

  Long createDocumentReminder(ProcessingUser processingUser);

  String pushMobileNotification(MobileNotificationMessageDto mobileNotificationMessageDto)
      throws FirebaseMessagingException;

  void pushMobileNotificationsByUserId(
      MobileNotificationMessageDto mobileNotificationMessageDto, Long userId)
      throws FirebaseMessagingException;

  MobileNotificationMessageDto buildMobileNotificationMessage(
      DocumentReminderStatusEnum documentReminderStatus,
      String documentNumber);
}

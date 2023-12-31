package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.RESET_FOLDER_NEXT_NUMBER_BATCH_JOB;
import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB;

import edu.hcmus.doc.mainservice.model.dto.MobileNotificationMessageDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.util.DocDateTimeUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class DocScheduleService {

  private final FolderService folderService;

  private final DocumentReminderService documentReminderService;

  @Scheduled(cron = "${doc.schedule.reset-folder-next-number-cron}")
  public void resetFolderNextNumber() {
    log.info(RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.info);
    SecurityUtils.setSecurityContextForBatchJob(RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.value);

    folderService.resetFolderNextNumberAndUpdateYear();
  }

  /**
   * Run at 12:00:05 AM every day
   */
  @Scheduled(cron = "${doc.schedule.update-document-reminder-status-cron}")
  public void updateDocumentReminderStatus() {
    log.info(UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB.info);
    SecurityUtils.setSecurityContextForBatchJob(UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB.value);

    List<DocumentReminder> reminders = documentReminderService.getDocumentRemindersByStatusIn(
        List.of(DocumentReminderStatusEnum.ACTIVE,
            DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION));

    reminders.forEach(reminder -> {
      LocalDateTime expirationDateTime = DocDateTimeUtils.getAtEndOfDay(reminder.getExpirationDate());

      if (expirationDateTime.isBefore(LocalDateTime.now())) {
        reminder.setStatus(DocumentReminderStatusEnum.EXPIRED);
      }
      else if (DocDateTimeUtils.isBetween(LocalDateTime.now(), DocDateTimeUtils.getAtStartOf7DaysBefore(reminder.getExpirationDate()), expirationDateTime)) {
        reminder.setStatus(DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION);
      }

      try {
        MobileNotificationMessageDto message;
        if (reminder.getStatus() != DocumentReminderStatusEnum.ACTIVE) {
          if (Objects.nonNull(reminder.getProcessingUser().getProcessingDocument().getIncomingDoc())){
            message = documentReminderService.buildMobileNotificationMessage(reminder.getStatus(),
                reminder.getProcessingUser().getProcessingDocument().getIncomingDoc().getIncomingNumber());
            message.setProcessingDocumentType(ProcessingDocumentTypeEnum.INCOMING_DOCUMENT);
            message.setDocumentId(reminder.getProcessingUser().getProcessingDocument().getIncomingDoc().getId());
          } else {
            message = documentReminderService.buildMobileNotificationMessage(reminder.getStatus(),
                reminder.getProcessingUser().getProcessingDocument().getOutgoingDocument().getOutgoingNumber());
            message.setProcessingDocumentType(ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT);
            message.setDocumentId(reminder.getProcessingUser().getProcessingDocument().getOutgoingDocument().getId());
          }

          documentReminderService.pushMobileNotificationsByUserId(message, reminder.getProcessingUser().getUser().getId());
        }
      } catch (Exception e) {
        log.error("Error when sending mobile notification: {}", e.getMessage(), e);
      }
    });

    documentReminderService.saveAll(reminders);
  }
}

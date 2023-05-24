package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.RESET_FOLDER_NEXT_NUMBER_BATCH_JOB;
import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.ScheduleService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final FolderService folderService;
  private final TaskScheduler taskScheduler;

  private final DocumentReminderRepository documentReminderRepository;

  @Override
  @Scheduled(cron = "${doc.schedule.reset-folder-next-number-cron}")
  public void resetFolderNextNumber() {
    log.info(RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.info);
    SecurityUtils.setSecurityContextForBatchJob(RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.value);
    folderService.resetFolderNextNumberAndUpdateYear();
  }

  @Override
  public void changeDocumentReminderStatus(ProcessingUser processingUser, LocalDateTime timeExecution, DocumentReminderStatusEnum status) {
    Instant toInstant = timeExecution.toInstant(ZoneId.systemDefault()
            .getRules()
            .getOffset(LocalDateTime.now()));

    taskScheduler.schedule(() -> {
      log.info(UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB.info);
      SecurityUtils.setSecurityContextForBatchJob(UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB.value);
      documentReminderRepository.findByProcessingUserId(processingUser.getId())
          .ifPresent(documentReminder -> {
            documentReminder.setStatus(status);
            documentReminderRepository.save(documentReminder);
          });
    }, toInstant);
  }
}

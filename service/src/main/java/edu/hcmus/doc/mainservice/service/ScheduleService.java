package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDateTime;

public interface ScheduleService {

  void resetFolderNextNumber();

  void changeDocumentReminderStatus(ProcessingUser processingUser, LocalDateTime timeExecution,
      DocumentReminderStatusEnum status);
}

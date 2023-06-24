package edu.hcmus.doc.mainservice.model.enums;

import edu.hcmus.doc.mainservice.service.ScheduleService;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BatchJobEnum {

  DOC_BATCH_JOB_USER("Batch job user", "DOC_BATCH_JOB_USER", null),
  RESET_FOLDER_NEXT_NUMBER_BATCH_JOB(
      "Reset folder next number batch job is running",
      "RESET_FOLDER_NEXT_NUMBER_BATCH_JOB",
      ScheduleService::resetFolderNextNumber
  ),
  UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB(
      "Update document reminder status batch job is running",
      "UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB",
      ScheduleService::updateDocumentReminderStatus
  );

  public final String info;
  public final String value;
  public final Consumer<ScheduleService> executor;
}

package edu.hcmus.doc.mainservice.model.enums;

import edu.hcmus.doc.mainservice.service.impl.DocScheduleService;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BatchJobEnum {

  DOC_BATCH_JOB_USER("Batch job user", "DOC_BATCH_JOB_USER", null),
  RESET_FOLDER_NEXT_NUMBER_BATCH_JOB(
      "Reset folder next number batch job is running",
      "RESET_FOLDER_NEXT_NUMBER_BATCH_JOB",
      DocScheduleService::resetFolderNextNumber
  ),
  UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB(
      "Update document reminder status batch job is running",
      "UPDATE_DOCUMENT_REMINDER_STATUS_BATCH_JOB",
      DocScheduleService::updateDocumentReminderStatus
  );

  public final String info;
  public final String value;
  public final Consumer<DocScheduleService> executor;
}

package edu.hcmus.doc.mainservice.model.enums;

import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.FolderService;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BatchJobEnum {

  DOC_BATCH_JOB_USER("Batch job user", "DOC_BATCH_JOB_USER", null),
  RESET_FOLDER_NEXT_NUMBER_BATCH_JOB(
      "Reset folder next number batch job is running",
      "RESET_FOLDER_NEXT_NUMBER_BATCH_JOB",
      folderService -> {
        SecurityUtils.setSecurityContextForBatchJob("RESET_FOLDER_NEXT_NUMBER_BATCH_JOB");
        folderService.resetFolderNextNumberAndUpdateYear();
      }
  );

  public final String info;
  public final String value;
  public final Consumer<FolderService> executor;
}

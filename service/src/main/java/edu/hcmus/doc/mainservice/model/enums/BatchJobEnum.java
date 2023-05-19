package edu.hcmus.doc.mainservice.model.enums;

import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.FolderService;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BatchJobEnum {

  DOC_BATCH_JOB_USER("DOC_BATCH_JOB_USER", "User for batch job", (folderService) -> {}),
  RESET_FOLDER_NEXT_NUMBER_BATCH_JOB("RESET_FOLDER_NEXT_NUMBER_BATCH_JOB",
      "Reset folder next number batch job is running",
      folderService -> {
        SecurityUtils.setSecurityContextForBatchJob("RESET_FOLDER_NEXT_NUMBER_BATCH_JOB");
        folderService.resetFolderNextNumberAndUpdateYear();
      });

  public final String value;

  public final String info;

  public final Consumer<FolderService> executor;
}

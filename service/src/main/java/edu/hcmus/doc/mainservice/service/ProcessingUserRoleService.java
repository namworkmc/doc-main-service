package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface ProcessingUserRoleService {

  List<ProcessingDetailsDto> getProcessingUserRolesByDocumentId(
      ProcessingDocumentTypeEnum processingDocumentType, Long incomingDocumentId,
      boolean onlyAssignee);
}

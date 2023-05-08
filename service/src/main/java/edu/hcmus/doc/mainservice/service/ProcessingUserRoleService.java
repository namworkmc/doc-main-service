package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import java.util.List;

public interface ProcessingUserRoleService {

  List<ProcessingDetailsDto> getProcessingUserRolesByIncomingDocumentId(Long incomingDocumentId,
      boolean onlyAssignee);
}

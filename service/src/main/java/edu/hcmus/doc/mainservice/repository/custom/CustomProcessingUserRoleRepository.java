package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import java.util.List;

public interface CustomProcessingUserRoleRepository {

  List<ProcessingDetailsDto> getProcessingUserRolesByIncomingDocumentId(Long incomingDocumentId, boolean onlyAssignee);
}

package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface CustomProcessingUserRoleRepository {

  List<ProcessingDetailsDto> getProcessingUserRolesByTypeAndDocumentId(
      ProcessingDocumentTypeEnum processingDocumentType, Long documentId, boolean onlyAssignee);

  ProcessingUserRole findByProcessingUserId(Long processingUserId);
}

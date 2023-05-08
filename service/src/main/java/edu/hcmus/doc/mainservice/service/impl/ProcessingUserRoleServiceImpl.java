package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.service.ProcessingUserRoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingUserRoleServiceImpl implements ProcessingUserRoleService {

  private final ProcessingUserRoleRepository processingUserRoleRepository;

  @Override
  public List<ProcessingDetailsDto> getProcessingUserRolesByIncomingDocumentId(Long incomingDocumentId, boolean onlyAssignee) {
    return processingUserRoleRepository.getProcessingUserRolesByIncomingDocumentId(incomingDocumentId, onlyAssignee);
  }
}

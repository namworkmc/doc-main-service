package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
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
  private final ProcessingDocumentRepository processingDocumentRepository;
  private final OutgoingDocumentRepository outgoingDocumentRepository;

  @Override
  public List<ProcessingDetailsDto> getProcessingUserRolesByDocumentId(
      ProcessingDocumentTypeEnum processingDocumentType, Long documentId, boolean onlyAssignee) {
    List<ProcessingDetailsDto> processingDetailsDtoList = processingUserRoleRepository.getProcessingUserRolesByTypeAndDocumentId(
        processingDocumentType, documentId, onlyAssignee);
    processingDetailsDtoList.forEach(processingDetailsDto -> {
      boolean isDocumentClosedOrReleased = false;

      if (processingDocumentType.equals(ProcessingDocumentTypeEnum.INCOMING_DOCUMENT)) {
        isDocumentClosedOrReleased = processingDocumentRepository.isDocumentClosed(documentId,
            processingDocumentType);
      } else {
        isDocumentClosedOrReleased = outgoingDocumentRepository.isDocumentReleased(documentId);
      }

      processingDetailsDto.setIsDocClosedOrReleased(isDocumentClosedOrReleased);
    });
    return processingDetailsDtoList;
  }
}

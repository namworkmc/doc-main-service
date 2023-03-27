package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class IncomingDocumentMapperDecorator implements IncomingDocumentMapper {

  @Autowired
  DocumentTypeService documentTypeService;

  @Autowired
  DistributionOrganizationService distributionOrganizationService;

  @Autowired
  FolderService folderService;

  @Autowired
  @Qualifier("delegate")
  private IncomingDocumentMapper delegate;

  @Override
  public IncomingDocumentDto toDto(IncomingDocument incomingDocument) {
    return delegate.toDto(incomingDocument);
  }

  @Override
  public IncomingDocumentDto toDto(ProcessingDocument processingDocument) {
    IncomingDocumentDto dto = delegate.toDto(processingDocument.getIncomingDoc());
    dto.setStatus(processingDocument.getStatus());
    dto.setProcessingDuration(processingDocument.getProcessingDuration());
    return dto;
  }

  @Override
  public IncomingDocument toEntity(IncomingDocumentPostDto dto) {
    IncomingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setDistributionOrg(distributionOrganizationService.findById(dto.getDistributionOrg()));
    entity.setSendingLevel(null);

    return entity;
  }
}

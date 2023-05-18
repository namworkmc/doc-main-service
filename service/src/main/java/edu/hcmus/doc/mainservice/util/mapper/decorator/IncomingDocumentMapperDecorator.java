package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.util.TransferDocumentUtils;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import java.util.List;
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
  AttachmentService attachmentService;

  @Autowired
  ProcessingDocumentService processingDocumentService;

  @Autowired
  @Qualifier("delegate")
  private IncomingDocumentMapper delegate;

  @Override
  public IncomingDocumentDto toDto(IncomingDocument incomingDocument) {
    return delegate.toDto(incomingDocument);
  }

  @Override
  public IncomingDocumentDto toDto(ProcessingDocument processingDocument) {
    List<AttachmentDto> attachments = attachmentService.getAttachmentsByIncomingDocId(
        processingDocument.getIncomingDoc().getId());

    IncomingDocumentDto dto = delegate.toDto(processingDocument.getIncomingDoc());
    dto.setStatus(processingDocument.getStatus());
    dto.setAttachments(attachments);

    User currentUser = SecurityUtils.getCurrentUser();
    int step = TransferDocumentUtils.getStep(currentUser, null, true);
    Boolean isDocTransferred = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .incomingDocumentId(processingDocument.getIncomingDoc().getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.REPORTER)
            .step(step)
            .build());
    dto.setIsDocTransferred(isDocTransferred);

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

  @Override
  public IncomingDocument toEntity(IncomingDocumentPutDto dto) {
    IncomingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setDistributionOrg(distributionOrganizationService.findById(dto.getDistributionOrg()));
    entity.setSendingLevel(null);

    return entity;
  }
}


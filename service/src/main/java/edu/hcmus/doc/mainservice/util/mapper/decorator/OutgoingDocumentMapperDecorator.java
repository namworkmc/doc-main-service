package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.PublishDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import edu.hcmus.doc.mainservice.util.TransferDocumentUtils;
import edu.hcmus.doc.mainservice.util.mapper.OutgoingDocumentMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class OutgoingDocumentMapperDecorator implements OutgoingDocumentMapper {

  @Autowired
  DocumentTypeService documentTypeService;

  @Autowired
  DepartmentService departmentService;

  @Autowired
  FolderService folderService;

  @Autowired
  ProcessingDocumentService processingDocumentService;

  @Autowired
  OutgoingDocumentRepository outgoingDocumentRepository;

  @Autowired
  OutgoingDocumentService outgoingDocumentService;

  @Autowired
  AttachmentService attachmentService;

  @Autowired
  @Qualifier("delegate")
  private OutgoingDocumentMapper delegate;

  @Override
  public OutgoingDocument toEntity(OutgoingDocumentPostDto dto) {
    OutgoingDocument entity = delegate.toEntity(dto);

    entity.setStatus(OutgoingDocumentStatusEnum.UNPROCESSED);
    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setPublishingDepartment(departmentService.getDepartmentById(dto.getPublishingDepartment()));

    return entity;
  }

  @Override
  public OutgoingDocument toEntity(OutgoingDocumentPutDto dto) {
    OutgoingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setPublishingDepartment(departmentService.getDepartmentById(dto.getPublishingDepartment()));

    return entity;
  }

  @Override
  public OutgoingDocument toEntity(PublishDocumentDto dto) {
    OutgoingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setPublishingDepartment(departmentService.getDepartmentById(dto.getPublishingDepartment()));

    return entity;
  }

  @Override
  public OutgoingDocumentGetDto toDto(OutgoingDocument outgoingDocument) {
    List<AttachmentDto> attachments = attachmentService.getAttachmentsByDocId(
        outgoingDocument.getId(), ParentFolderEnum.OGD);

    OutgoingDocumentGetDto dto = delegate.toDto(outgoingDocument);
    User currentUser = SecurityUtils.getCurrentUser();

    int step = TransferDocumentUtils.getStepOutgoingDocument(currentUser, true);
    Boolean isDocTransferred = processingDocumentService.isUserWorkingOnOutgoingDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(outgoingDocument.getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.REPORTER)
            .step(step)
            .build());

    int collaboratorStep = TransferDocumentUtils.getStepOutgoingDocument(currentUser, false);
    Boolean isDocCollaborator = processingDocumentService.isUserWorkingOnOutgoingDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(outgoingDocument.getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.COLLABORATOR)
            .step(collaboratorStep)
            .build());

    dto.setStatus(outgoingDocument.getStatus());
    dto.setIsDocTransferred(isDocTransferred);
    dto.setIsDocCollaborator(isDocCollaborator);
    dto.setAttachments(attachments);
    dto.setIsTransferable(outgoingDocumentRepository.getOutgoingDocumentsWithTransferPermission().contains(outgoingDocument.getId()));
    dto.setIsReleasable(outgoingDocumentService.validateReleaseDocument(outgoingDocument));

    dto.setCustomProcessingDuration(
        processingDocumentService
            .getDateExpiredV2(outgoingDocument.getId(), currentUser.getId(),
                currentUser.getRole(), true, ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT)
            .map(result -> result.equals("infinite") ? DocMessageUtils.getContent(
                MESSAGE.infinite_processing_duration) : LocalDate.parse(result).format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")))
            .orElse(null)
    );

    return dto;
  }
}


package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.util.mapper.OutgoingDocumentMapper;
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
}


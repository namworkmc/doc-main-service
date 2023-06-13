package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.PublishDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.util.mapper.decorator.OutgoingDocumentMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {
        DocumentTypeMapper.class,
        DepartmentMapper.class,
        FolderMapper.class,
        UserMapper.class
    }
)
@DecoratedWith(OutgoingDocumentMapperDecorator.class)
public interface OutgoingDocumentMapper {

  OutgoingDocumentGetDto toDto(OutgoingDocument entity);

  @Mapping(target = "status", ignore = true)
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "publishingDepartment", ignore = true)
  OutgoingDocument toEntity(OutgoingDocumentGetDto outgoingDocumentDto);

  @Mapping(target = "status", ignore = true)
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "publishingDepartment", ignore = true)
  OutgoingDocument toEntity(OutgoingDocumentPostDto outgoingDocumentDto);

  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "publishingDepartment", ignore = true)
  OutgoingDocument toEntity(OutgoingDocumentPutDto outgoingDocumentDto);

  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "publishingDepartment", ignore = true)
  OutgoingDocument toEntity(PublishDocumentDto outgoingDocumentDto);
}

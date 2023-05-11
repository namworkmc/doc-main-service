package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.util.mapper.decorator.OutgoingDocumentMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
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
//  @Mapping(source = "outgoingNumber", target = "outgoingNumber")
//  @Mapping(source = "recipient", target = "recipient")
//  @Mapping(source = "summary", target = "summary")
//  @Mapping(source = "urgency", target = "urgency")
//  @Mapping(source = "confidentiality", target = "confidentiality")
//  @Mapping(source = "releaseDate", target = "releaseDate")
  OutgoingDocumentGetDto toDto(OutgoingDocument entity);

//  @Mapping(source = "recipient", target = "recipient")
//  @Mapping(source = "summary", target = "summary")
//  @Mapping(source = "urgency", target = "urgency")
//  @Mapping(source = "confidentiality", target = "confidentiality")
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "publishingDepartment", ignore = true)
  OutgoingDocument toEntity(OutgoingDocumentPostDto outgoingDocumentDto);
}

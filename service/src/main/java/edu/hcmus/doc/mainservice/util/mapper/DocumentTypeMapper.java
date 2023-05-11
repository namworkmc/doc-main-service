package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING)
public interface DocumentTypeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    DocumentTypeDto toDto(DocumentType documentType);

  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  DocumentType toEntity(DocumentTypeDto documentTypeDto);

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  DocumentType partialUpdate(DocumentTypeDto documentTypeDto, @MappingTarget DocumentType documentType);
}

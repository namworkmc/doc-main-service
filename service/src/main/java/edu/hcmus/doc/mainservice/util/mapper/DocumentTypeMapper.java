package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentTypeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    DocumentTypeDto toDto(DocumentType documentType);
}

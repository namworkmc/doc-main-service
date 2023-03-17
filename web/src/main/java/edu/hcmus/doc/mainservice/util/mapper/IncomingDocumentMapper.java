package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(IncomingDocumentMapperDecorator.class)
public interface IncomingDocumentMapper {

  @Mapping(target = "status", ignore = true)
  @Mapping(target = "processingDuration", ignore = true)
  IncomingDocumentDto toDto(IncomingDocument incomingDocument);

  @Mapping(target = "summary", ignore = true)
  @Mapping(target = "sendingLevel", ignore = true)
  @Mapping(target = "originalSymbolNumber", ignore = true)
  @Mapping(target = "incomingNumber", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "distributionOrg", ignore = true)
  @Mapping(target = "arrivingDate", ignore = true)
  IncomingDocumentDto toDto(ProcessingDocument processingDocument);
}

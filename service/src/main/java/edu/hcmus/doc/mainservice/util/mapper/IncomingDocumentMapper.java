package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.util.mapper.decorator.IncomingDocumentMapperDecorator;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {
        DocumentTypeMapper.class,
        DistributionOrganizationMapper.class,
        FolderMapper.class,
        UserMapper.class
    }
)
@DecoratedWith(IncomingDocumentMapperDecorator.class)
public interface IncomingDocumentMapper {

  @Mapping(target = "status", ignore = true)
  @Mapping(target = "processingDuration", ignore = true)
  @Mapping(source = "urgency", target = "urgency")
  @Mapping(source = "confidentiality", target = "confidentiality")
  @Mapping(source = "distributionDate", target = "distributionDate")
  @Mapping(source = "arrivingDate", target = "arrivingDate")
  @Mapping(source = "arrivingTime", target = "arrivingTime")
  @Mapping(source = "sendingLevel", target = "sendingLevel", ignore = true)
  IncomingDocumentDto toDto(IncomingDocument incomingDocument);

  @Mapping(target = "summary", ignore = true)
  @Mapping(target = "sendingLevel", ignore = true)
  @Mapping(target = "originalSymbolNumber", ignore = true)
  @Mapping(target = "incomingNumber", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "distributionOrg", ignore = true)
  @Mapping(target = "arrivingDate", ignore = true)
  @Mapping(target = "folder", ignore = true)
  IncomingDocumentDto toDto(ProcessingDocument processingDocument);

  @Mapping(source = "incomingNumber", target = "incomingNumber")
  @Mapping(source = "originalSymbolNumber", target = "originalSymbolNumber")
  @Mapping(source = "distributionDate", target = "distributionDate")
  @Mapping(source = "arrivingDate", target = "arrivingDate")
  @Mapping(source = "arrivingTime", target = "arrivingTime")
  @Mapping(source = "summary", target = "summary")
  @Mapping(source = "urgency", target = "urgency")
  @Mapping(source = "confidentiality", target = "confidentiality")
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "distributionOrg", ignore = true)
  @Mapping(target = "sendingLevel", ignore = true)
  IncomingDocument toEntity(IncomingDocumentPostDto incomingDocumentDto);

  @Mapping(source = "incomingNumber", target = "incomingNumber")
  @Mapping(source = "originalSymbolNumber", target = "originalSymbolNumber")
  @Mapping(source = "distributionDate", target = "distributionDate")
  @Mapping(source = "arrivingDate", target = "arrivingDate")
  @Mapping(source = "arrivingTime", target = "arrivingTime")
  @Mapping(source = "summary", target = "summary")
  @Mapping(source = "urgency", target = "urgency")
  @Mapping(source = "confidentiality", target = "confidentiality")
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "distributionOrg", ignore = true)
  @Mapping(target = "sendingLevel", ignore = true)
  @Mapping(source = "id", target = "id")
  IncomingDocument toEntity(IncomingDocumentPutDto incomingDocumentDto);

  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "distributionOrg", ignore = true)
  @Mapping(target = "folder", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(IncomingDocumentPutDto incomingDocumentDto, @MappingTarget IncomingDocument incomingDocument);
}

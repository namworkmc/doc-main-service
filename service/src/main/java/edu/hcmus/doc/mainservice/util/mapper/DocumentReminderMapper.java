package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderWrapperDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.util.mapper.decorator.DocumentReminderMapperDecorator;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING, imports = {ProcessingDocumentTypeEnum.class})
@DecoratedWith(DocumentReminderMapperDecorator.class)
public interface DocumentReminderMapper {

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  DocumentReminder toEntity(DocumentReminderDetailsDto documentReminderDetailsDto);

  DocumentReminderDetailsDto toDto(DocumentReminder documentReminder);

  @Mapping(target = "documentId", source = "processingDocument", qualifiedByName = "mapDocumentId")
  @Mapping(target = "documentNumber", source = "processingDocument", qualifiedByName = "mapDocumentNumber")
  @Mapping(target = "documentName", source = "processingDocument", qualifiedByName = "mapDocumentName")
  @Mapping(target = "summary", source = "processingDocument", qualifiedByName = "mapDocumentSummary")
  @Mapping(target = "expirationDate", source = "date")
  @Mapping(
      target = "documentType",
      expression
          = "java("
          + "processingDocument.isIncomingDocument()"
          + "? ProcessingDocumentTypeEnum.INCOMING_DOCUMENT"
          + ": ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT)"
  )
  DocumentReminderDetailsDto toDto(ProcessingDocument processingDocument, LocalDate date);

  DocumentReminderWrapperDto toDto(Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> processingDocumentReminderMap, LocalDate date);

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  DocumentReminder partialUpdate(
      DocumentReminderDetailsDto documentReminderDetailsDto, @MappingTarget DocumentReminder documentReminder);

  @Named("mapDocumentId")
  default Long mapDocumentId(ProcessingDocument processingDocument) {
    return processingDocument.isIncomingDocument()
        ? processingDocument.getIncomingDoc().getId()
        : processingDocument.getOutgoingDocument().getId();
  }

  @Named("mapDocumentNumber")
  default String mapDocumentNumber(ProcessingDocument processingDocument) {
    return processingDocument.isIncomingDocument()
        ? processingDocument.getIncomingDoc().getIncomingNumber()
        : processingDocument.getOutgoingDocument().getOutgoingNumber();
  }

  @Named("mapDocumentName")
  default String mapDocumentName(ProcessingDocument processingDocument) {
    return processingDocument.isIncomingDocument()
        ? processingDocument.getIncomingDoc().getName()
        : processingDocument.getOutgoingDocument().getName();
  }

  @Named("mapDocumentSummary")
  default String mapDocumentSummary(ProcessingDocument processingDocument) {
    return processingDocument.isIncomingDocument()
        ? processingDocument.getIncomingDoc().getSummary()
        : processingDocument.getOutgoingDocument().getSummary();
  }
}

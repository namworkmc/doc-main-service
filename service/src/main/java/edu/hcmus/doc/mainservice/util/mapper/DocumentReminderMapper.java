package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderWrapperDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
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
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING)
@DecoratedWith(DocumentReminderMapperDecorator.class)
public interface DocumentReminderMapper {

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  DocumentReminder toEntity(DocumentReminderDetailsDto documentReminderDetailsDto);

  DocumentReminderDetailsDto toDto(DocumentReminder documentReminder);

  @Mapping(target = "incomingDocumentId", source = "processingDocument.incomingDoc.id")
  @Mapping(target = "incomingNumber", source = "processingDocument.incomingDoc.incomingNumber")
  @Mapping(target = "documentName", source = "processingDocument.incomingDoc.name")
  @Mapping(target = "summary", source = "processingDocument.incomingDoc.summary")
  @Mapping(target = "expirationDate", source = "date")
  @Mapping(target = "status", ignore = true)
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
}

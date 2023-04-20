package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.util.mapper.decorator.DocumentReminderMapperDecorator;
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
  @Mapping(target = "processingDoc", ignore = true)
  @Mapping(target = "executionTime", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  DocumentReminder toEntity(DocumentReminderDetailsDto documentReminderDetailsDto);

  @Mapping(target = "incomingNumber", source = "processingDoc.incomingDoc.incomingNumber")
  @Mapping(target = "summary", source = "processingDoc.incomingDoc.summary")
  DocumentReminderDetailsDto toDto(DocumentReminder documentReminder);

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "processingDoc", ignore = true)
  @Mapping(target = "executionTime", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  DocumentReminder partialUpdate(
      DocumentReminderDetailsDto documentReminderDetailsDto, @MappingTarget DocumentReminder documentReminder);
}

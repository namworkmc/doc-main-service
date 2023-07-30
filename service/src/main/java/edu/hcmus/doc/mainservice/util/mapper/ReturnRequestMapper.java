package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.util.mapper.decorator.ReturnRequestMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
@DecoratedWith(ReturnRequestMapperDecorator.class)
public interface ReturnRequestMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "currentProcessingUserId", ignore = true)
  @Mapping(target = "previousProcessingUserId", ignore = true)
  @Mapping(target = "documentId", ignore = true)
  @Mapping(target = "reason", ignore = true)
  ReturnRequestDto toDto(ReturnRequest entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "currentProcessingUserId", ignore = true)
  @Mapping(target = "currentProcessingUserFullName", ignore = true)
  @Mapping(target = "currentProcessingUserRole", ignore = true)
  @Mapping(target = "currentProcessingUserRoleTitle", ignore = true)
  @Mapping(target = "previousProcessingUserId", ignore = true)
  @Mapping(target = "previousProcessingUserFullName", ignore = true)
  @Mapping(target = "previousProcessingUserRole", ignore = true)
  @Mapping(target = "previousProcessingUserRoleTitle", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "documentId", ignore = true)
  @Mapping(target = "documentType", ignore = true)
  @Mapping(target = "reason", ignore = true)
  @Mapping(target = "returnRequestType", ignore = true)
  ReturnRequestGetDto toReturnRequestGetDto(ReturnRequest entity, ProcessingDocumentTypeEnum type);

}

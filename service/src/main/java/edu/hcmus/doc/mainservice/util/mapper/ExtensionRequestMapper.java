package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.ExtensionRequestDto;
import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ExtensionRequestMapper {

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "processingUser", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "validatedBy", ignore = true)
  ExtensionRequest toEntity(ExtensionRequestDto extensionRequestDto);

  @Mapping(target = "processingUserId", ignore = true)
  @Mapping(source = "validatedBy.id", target = "validatorId")
  ExtensionRequestDto toDto(ExtensionRequest extensionRequest);

  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "processingUser", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "validatedBy", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ExtensionRequest partialUpdate(ExtensionRequestDto extensionRequestDto,
      @MappingTarget ExtensionRequest extensionRequest);
}

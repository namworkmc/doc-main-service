package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.ProcessingMethodDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
//@DecoratedWith(DepartmentMapperDecorator.class)
public interface ProcessingMethodMapper {

//  @Mapping(target = "updatedDate", ignore = true)
//  @Mapping(target = "updatedBy", ignore = true)
//  @Mapping(target = "deleted", ignore = true)
//  @Mapping(target = "createdDate", ignore = true)
//  @Mapping(target = "createdBy", ignore = true)
  ProcessingMethod toEntity(ProcessingMethodDto processingMethodDto);

  ProcessingMethodDto toDto(ProcessingMethod processingMethod);
}

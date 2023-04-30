package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING)
public interface DepartmentMapper {

  Department toEntity(DepartmentDto departmentDto);

  DepartmentDto toDto(Department department);

  Department partialUpdate(DepartmentDto departmentDto, @MappingTarget Department department);
}

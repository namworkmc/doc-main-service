package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.util.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

public abstract class DepartmentMapperDecorator implements DepartmentMapper {

  @Autowired
  @Qualifier("delegate")
  private DepartmentMapper delegate;

  @Lazy
  @Autowired
  private UserService userService;

  @Override
  public Department toEntity(DepartmentDto departmentDto) {
    Department entity = delegate.toEntity(departmentDto);
    if (departmentDto.getTruongPhong() != null && departmentDto.getTruongPhong().getId() != null) {
      entity.setTruongPhong(userService.getUserById(departmentDto.getTruongPhong().getId()));
    }
    return entity;
  }

  @Override
  public Department partialUpdate(DepartmentDto departmentDto, Department department) {
    Department entity = delegate.partialUpdate(departmentDto, department);
    if (departmentDto.getTruongPhong() != null) {
      entity.setTruongPhong(userService.getUserById(departmentDto.getTruongPhong().getId()));
    }

    return entity;
  }
}

package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

@Data
public class UserDepartmentDto extends UserDto {
  private String departmentName;
}

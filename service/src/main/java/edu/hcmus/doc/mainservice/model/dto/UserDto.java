package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import lombok.Data;

@Data
public class UserDto extends DocAbstractDto {

  private String username;
  private String email;
  private String fullName;
  private DocSystemRoleEnum role;
  private DepartmentDto department;
}

package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto extends DocAbstractDto {

  @NotBlank(message = "user.detail.username_required")
  private String username;

  @Email(message = "user.detail.email_invalid")
  @NotBlank(message = "user.detail.email_invalid")
  private String email;

  @NotBlank(message = "user.detail.full_name_required")
  private String fullName;

  private String password;

  @NotNull(message = "user.detail.role_required")
  private DocSystemRoleEnum role;

  @NotNull(message = "user.detail.department_required")
  private DepartmentDto department;
}

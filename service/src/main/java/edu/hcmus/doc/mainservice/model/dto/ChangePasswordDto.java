package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.util.validator.annotation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDto {

  @PasswordValidator
  private String oldPassword;

  @PasswordValidator
  private String newPassword;

  @PasswordValidator
  private String confirmPassword;
}

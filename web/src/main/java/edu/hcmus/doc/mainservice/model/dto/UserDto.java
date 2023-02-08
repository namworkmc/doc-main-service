package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocRoleEnum;
import java.util.Set;
import lombok.Data;

@Data
public class UserDto {

  private Long id;
  private String username;
  private String email;
  private Set<DocRoleEnum> roles;
}

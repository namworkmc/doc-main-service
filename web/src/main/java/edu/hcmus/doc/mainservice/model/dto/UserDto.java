package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.Set;
import lombok.Data;

@Data
public class UserDto extends DocAbstractDto {

  private String username;
  private String email;
  private Set<DocSystemRoleEnum> roles;
}

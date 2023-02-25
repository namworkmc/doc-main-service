package edu.hcmus.doc.mainservice.model.dto;

import java.util.Set;
import lombok.Data;

@Data
public class UserDto {

  private Long id;
  private String username;
  private String email;
  private Set<String> roles;
}

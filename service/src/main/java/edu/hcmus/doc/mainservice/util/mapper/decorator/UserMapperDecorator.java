package edu.hcmus.doc.mainservice.util.mapper.decorator;

import static java.util.stream.Collectors.toSet;

import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.service.UserRoleService;
import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserMapperDecorator implements UserMapper {

  @Autowired
  @Qualifier("delegate")
  private UserMapper delegate;

  @Autowired
  private UserRoleService userRoleService;

  @Override
  public UserDto toDto(User user) {
    UserDto dto = delegate.toDto(user);
    dto.setRoles(userRoleService.getUserRolesByUserId(user.getId())
        .stream()
        .map(UserRole::getRole)
        .collect(toSet()));
    return dto;
  }
}

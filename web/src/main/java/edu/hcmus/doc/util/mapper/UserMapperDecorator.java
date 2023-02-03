package edu.hcmus.doc.util.mapper;

import static java.util.stream.Collectors.toSet;

import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.model.enums.DocRoleEnum;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserMapperDecorator implements UserMapper {

  @Autowired
  @Qualifier("delegate")
  private UserMapper delegate;

  @Override
  public UserDto toDto(User user) {
    UserDto dto = delegate.toDto(user);
    dto.setRoles(mapRoles(user));
    return dto;
  }

  private Set<DocRoleEnum> mapRoles(User user) {
    if (CollectionUtils.isEmpty(user.getRoles())) {
      return Collections.emptySet();
    }

    return user
        .getRoles()
        .stream()
        .map(userRole -> userRole.getRole().getName())
        .collect(toSet());
  }
}

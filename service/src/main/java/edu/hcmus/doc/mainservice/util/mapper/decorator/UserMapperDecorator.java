package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserMapperDecorator implements UserMapper {

  @Autowired
  @Qualifier("delegate")
  private UserMapper delegate;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDto toDto(User user) {
    return delegate.toDto(user);
  }

  @Override
  public List<UserDto> toDto(List<User> users) {
    return delegate.toDto(users);
  }

  @Override
  public User toEntity(UserDto userDto) {
    User user = delegate.toEntity(userDto);
    mapPasswordAndDepartment(userDto, user);
    return user;
  }

  @Override
  public User partialUpdate(UserDto userDto, User user) {
    mapPasswordAndDepartment(userDto, user);
    return delegate.partialUpdate(userDto, user);
  }

  private void mapPasswordAndDepartment(UserDto userDto, User user) {
    if (userDto.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
    if (userDto.getDepartment().getId() != null) {
      user.setDepartment(departmentService.getDepartmentById(userDto.getDepartment().getId()));
    }
  }
}

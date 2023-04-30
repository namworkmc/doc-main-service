package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/users")
public class UserController extends DocAbstractController {

  private final UserService userService;

  @GetMapping("/current")
  public UserDto getCurrentUser() {
    return userDecoratorMapper.toDto(userService.getCurrentUserFromDB());
  }

  @GetMapping("/role/{role}")
  public List<UserDto> getUsersByRole(@PathVariable DocSystemRoleEnum role) {
    return userMapper.toDto(userService.getUsersByRole(role));
  }

  @GetMapping("/role/department/{role}")
  public List<UserDepartmentDto> getUsersByRoleWithDepartment(@PathVariable DocSystemRoleEnum role) {
    return userService.getUsersByRoleWithDepartment(role);
  }

  @GetMapping("/current-principal")
  public UserDto getCurrentName() {
    return userMapper.toDto(SecurityUtils.getCurrentUser());
  }

  @PutMapping("/current/password")
  public Long updateCurrentUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
    return userService.updateCurrentUserPassword(oldPassword, newPassword);
  }

  @PutMapping("/current")
  public Long updateCurrentUser(@RequestBody UserDto userDto) {
    User user = userDecoratorMapper.partialUpdate(userDto, userService.getCurrentUserFromDB());
    return userService.updateCurrentUser(user);
  }
}

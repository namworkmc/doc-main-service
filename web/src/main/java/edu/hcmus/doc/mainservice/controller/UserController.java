package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @GetMapping("/directors")
  public List<UserDto> getDirectors() {
    return userMapper.toDto(userService.getDirectors());
  }

  @GetMapping("/current-principal")
  public UserDto getCurrentName() {
    return userMapper.toDto(SecurityUtils.getCurrentUser());
  }

  @GetMapping("/secretaries")
  public List<UserDto> getSecretaries() {
    return userMapper.toDto(userService.getSecretaries());
  }

  @GetMapping("/experts")
  public List<UserDto> getExperts() {
    return userMapper.toDto(userService.getExperts());
  }

  @GetMapping("/managers")
  public List<UserDto> getManagers() {
    return userMapper.toDto(userService.getManagers());
  }
}

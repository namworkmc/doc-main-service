package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.CredentialsDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/keycloak/users")
public class KeycloakUserController extends DocAbstractController {

  private final UserService userService;

  // GET
  @GetMapping
  public List<UserDto> getUsers(
      @RequestParam(required = false) String query,
      @RequestParam(required = false, defaultValue = "0") long first,
      @RequestParam(required = false, defaultValue = "10") long max) {
    return userService
        .getUsers(query, first, max)
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @GetMapping("/{id}")
  public UserDto getUserById(@PathVariable Long id) {
    return userDecoratorMapper.toDto(userService.getUserById(id));
  }

  @GetMapping("/username/{username}")
  public UserDto getUserByUsername(@PathVariable String username) {
    return userDecoratorMapper.toDto(userService.getUserByUsername(username));
  }

  @GetMapping("/email/{email}")
  public UserDto getUserByEmail(@PathVariable String email) {
    return userDecoratorMapper.toDto(userService.getUserByEmail(email));
  }

  @GetMapping("/total-users")
  public long getTotalUsers() {
    return userService.getTotalUsers();
  }

  @GetMapping("/current-name")
  public String getCurrentName() {
    return SecurityUtils.getCurrentName();
  }

  // POST
  @PostMapping("/{id}/auth/validate-credentials")
  public boolean getCredentials(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
    return userService.validateUserCredentialsByUserId(id, credentialsDto.getPassword());
  }
}

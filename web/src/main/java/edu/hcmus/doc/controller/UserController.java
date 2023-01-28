package edu.hcmus.doc.controller;

import edu.hcmus.doc.DocURL;
import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.service.UserService;
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

  @GetMapping
  public List<UserDto> getUsers() {
    return userService
        .getUsers()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }
}

package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.service.UserService;
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
    return userDecoratorMapper.toDto(userService.getCurrentUser());
  }
}

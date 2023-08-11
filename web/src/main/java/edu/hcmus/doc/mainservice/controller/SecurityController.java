package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.TokenDto;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.service.KeycloakService;
import edu.hcmus.doc.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/security")
public class SecurityController {

  private final KeycloakService keycloakService;

  private final DocumentReminderService documentReminderService;

  private final UserService userService;

  @PostMapping(value = "/auth/token")
  public ResponseEntity<TokenDto> login(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam(required = false) String firebaseCloudMessagingToken) {
    return ResponseEntity.ok().body(keycloakService.getToken(username, password, firebaseCloudMessagingToken));
  }

  @PostMapping("/auth/refresh-token")
  public ResponseEntity<TokenDto> refreshToken(@RequestParam String refreshToken) {
    return ResponseEntity.ok().body(keycloakService.refreshToken(refreshToken));
  }

  @PostMapping("/auth/token/revoke")
  public ResponseEntity<Void> revokeTokens(
      @RequestParam(defaultValue = "") String refreshToken,
      @RequestParam(required = false, defaultValue = "") String firebaseCloudMessagingToken) {
    keycloakService.revokeTokens(refreshToken, firebaseCloudMessagingToken);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/auth/forgot-password")
  public ResponseEntity<Long> forgotPassword(@RequestParam String email) {
    return ResponseEntity.ok().body(userService.resetUserPasswordByEmail(email));
  }
}

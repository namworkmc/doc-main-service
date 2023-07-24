package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.TokenDto;

public interface KeycloakService {

  TokenDto getToken(String username, String password, String firebaseToken);

  TokenDto refreshToken(String refreshToken);

  void revokeTokens(String refreshToken, String firebaseToken);
}

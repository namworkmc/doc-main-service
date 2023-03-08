package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.TokenDto;
import edu.hcmus.doc.mainservice.service.KeycloakService;
import edu.hcmus.doc.mainservice.util.KeycloakProperty;
import edu.hcmus.doc.mainservice.util.KeycloakResource;
import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class KeycloakServiceImpl implements KeycloakService {

  @AllArgsConstructor
  enum GrantType {
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token");

    private final String value;
  }

  private final KeycloakProperty keycloakProperty;

  private final KeycloakResource keycloakResource;

  public KeycloakServiceImpl(KeycloakProperty keycloakProperty,
      ResteasyWebTarget resteasyWebTarget) {
    this.keycloakProperty = keycloakProperty;
    this.keycloakResource = resteasyWebTarget.proxy(KeycloakResource.class);
  }

  @Override
  public TokenDto getToken(String username, String password) {
    return keycloakResource.getToken(
        GrantType.PASSWORD.value,
        username,
        password,
        keycloakProperty.getScope(),
        keycloakProperty.getClientId(),
        keycloakProperty.getClientSecret()
    );
  }

  @Override
  public TokenDto refreshToken(String refreshToken) {
    return keycloakResource.refreshToken(
        GrantType.REFRESH_TOKEN.value,
        refreshToken,
        keycloakProperty.getScope(),
        keycloakProperty.getClientId(),
        keycloakProperty.getClientSecret()
    );
  }
}

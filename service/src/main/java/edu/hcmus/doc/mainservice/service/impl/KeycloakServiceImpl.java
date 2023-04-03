package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.TokenDto;
import edu.hcmus.doc.mainservice.service.KeycloakService;
import edu.hcmus.doc.mainservice.util.keycloak.KeycloakProperty;
import edu.hcmus.doc.mainservice.util.keycloak.KeycloakResource;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class KeycloakServiceImpl implements KeycloakService {

  private final KeycloakProperty keycloakProperty;

  private final KeycloakResource keycloakTokenEndpoint;

  private final KeycloakResource keycloakRevokeEndpoint;

  public KeycloakServiceImpl(
      KeycloakProperty keycloakProperty,
      ResteasyWebTarget resteasyTokenTarget,
      ResteasyWebTarget resteasyRevokeTarget
  ) {
    this.keycloakProperty = keycloakProperty;
    this.keycloakTokenEndpoint = resteasyTokenTarget.proxy(KeycloakResource.class);
    this.keycloakRevokeEndpoint = resteasyRevokeTarget.proxy(KeycloakResource.class);
  }

  @Override
  public TokenDto getToken(String username, String password) {
    return keycloakTokenEndpoint.getToken(
        OAuth2ParameterNames.PASSWORD,
        username,
        password,
        keycloakProperty.getScope(),
        keycloakProperty.getClientId(),
        keycloakProperty.getClientSecret()
    );
  }

  @Override
  public TokenDto refreshToken(String refreshToken) {
    return keycloakTokenEndpoint.refreshToken(
        OAuth2ParameterNames.REFRESH_TOKEN,
        refreshToken,
        keycloakProperty.getScope(),
        keycloakProperty.getClientId(),
        keycloakProperty.getClientSecret()
    );
  }

  @Override
  public void revokeTokens(String refreshToken) {
    if (StringUtils.isBlank(refreshToken)) {
      return;
    }

    keycloakRevokeEndpoint.revokeTokens(
        keycloakProperty.getClientId(),
        keycloakProperty.getClientSecret(),
        refreshToken,
        OAuth2ParameterNames.REFRESH_TOKEN
    );
  }
}

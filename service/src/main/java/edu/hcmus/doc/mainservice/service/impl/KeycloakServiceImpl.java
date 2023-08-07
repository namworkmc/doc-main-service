package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.annotation.PasswordValidator;
import edu.hcmus.doc.mainservice.model.dto.TokenDto;
import edu.hcmus.doc.mainservice.model.entity.DocFirebaseTokenEntity;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocFirebaseTokenType;
import edu.hcmus.doc.mainservice.model.exception.DocMandatoryFields;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserPasswordException;
import edu.hcmus.doc.mainservice.repository.FirebaseTokenRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.KeycloakService;
import edu.hcmus.doc.mainservice.util.keycloak.KeycloakProperties;
import edu.hcmus.doc.mainservice.util.keycloak.KeycloakResource;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class KeycloakServiceImpl implements KeycloakService {

  private final KeycloakProperties keycloakProperties;

  private final KeycloakResource keycloakTokenEndpoint;

  private final KeycloakResource keycloakRevokeEndpoint;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final FirebaseTokenRepository firebaseTokenRepository;

  public KeycloakServiceImpl(
      KeycloakProperties keycloakProperties,
      ResteasyWebTarget resteasyTokenTarget,
      ResteasyWebTarget resteasyRevokeTarget,
      UserRepository userRepository, PasswordEncoder passwordEncoder,
      FirebaseTokenRepository firebaseTokenRepository) {
    this.keycloakProperties = keycloakProperties;
    this.keycloakTokenEndpoint = resteasyTokenTarget.proxy(KeycloakResource.class);
    this.keycloakRevokeEndpoint = resteasyRevokeTarget.proxy(KeycloakResource.class);
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.firebaseTokenRepository = firebaseTokenRepository;
  }

  @Override
  public TokenDto getToken(String username, String password, String firebaseToken) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || !password.matches(PasswordValidator.PASSWORD_REGEX)) {
      throw new DocMandatoryFields("Username or password is invalid");
    }

    User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new UserPasswordException();
    }

    if (StringUtils.isNotBlank(firebaseToken)) {
      saveFirebaseToken(user, firebaseToken, DocFirebaseTokenType.FCM);
    }

    return keycloakTokenEndpoint.getToken(
        OAuth2ParameterNames.PASSWORD,
        username,
        password,
        keycloakProperties.getScope(),
        keycloakProperties.getClientId(),
        keycloakProperties.getClientSecret()
    );
  }

  @Override
  public TokenDto refreshToken(String refreshToken) {
    return keycloakTokenEndpoint.refreshToken(
        OAuth2ParameterNames.REFRESH_TOKEN,
        refreshToken,
        keycloakProperties.getScope(),
        keycloakProperties.getClientId(),
        keycloakProperties.getClientSecret()
    );
  }

  @Override
  public void revokeTokens(String refreshToken, String firebaseToken) {
    if (StringUtils.isBlank(refreshToken)) {
      return;
    }

    if (StringUtils.isNotBlank(firebaseToken)) {
      deleteByTokenAndType(firebaseToken, DocFirebaseTokenType.FCM);
    }

    keycloakRevokeEndpoint.revokeTokens(
        keycloakProperties.getClientId(),
        keycloakProperties.getClientSecret(),
        refreshToken,
        OAuth2ParameterNames.REFRESH_TOKEN
    );
  }

  private void saveFirebaseToken(User user, String token, DocFirebaseTokenType type) {
    DocFirebaseTokenEntity firebaseToken = new DocFirebaseTokenEntity();
    firebaseToken.setToken(token);
    firebaseToken.setType(type);
    firebaseToken.setUser(user);
    firebaseTokenRepository.save(firebaseToken);
  }

  private void deleteByTokenAndType(String firebaseToken, DocFirebaseTokenType type) {
    firebaseTokenRepository.deleteByTokenAndType(firebaseToken, type);
  }
}

package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.PasswordExpiration;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.repository.PasswordExpirationRepository;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import edu.hcmus.doc.mainservice.util.validator.annotation.PasswordValidator;
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
import java.time.LocalDateTime;
import java.util.Optional;
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

  private final PasswordExpirationRepository passwordExpirationRepository;

  public KeycloakServiceImpl(
      KeycloakProperties keycloakProperties,
      ResteasyWebTarget resteasyTokenTarget,
      ResteasyWebTarget resteasyRevokeTarget,
      UserRepository userRepository, PasswordEncoder passwordEncoder,
      FirebaseTokenRepository firebaseTokenRepository,
      PasswordExpirationRepository passwordExpirationRepository) {
    this.keycloakProperties = keycloakProperties;
    this.keycloakTokenEndpoint = resteasyTokenTarget.proxy(KeycloakResource.class);
    this.keycloakRevokeEndpoint = resteasyRevokeTarget.proxy(KeycloakResource.class);
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.firebaseTokenRepository = firebaseTokenRepository;
    this.passwordExpirationRepository = passwordExpirationRepository;
  }

  @Override
  public TokenDto getToken(String username, String password, String firebaseToken) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || !password.matches(PasswordValidator.PASSWORD_REGEX)) {
      throw new DocMandatoryFields(DocMessageUtils.getContent(MESSAGE.invalid_login_information));
    }

    User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new UserPasswordException();
    }

    validatePasswordExpiration(user);


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

  private void validatePasswordExpiration(User user) {
    Optional<PasswordExpiration> passwordExpiration = passwordExpirationRepository.findLastPasswordExpirationByUserId(
        user.getId());
    // if present check
    if (passwordExpiration.isPresent()) {
      PasswordExpiration passwordExpirationEntity = passwordExpiration.get();

      LocalDateTime runTime = LocalDateTime.now();
      LocalDateTime creationTime = passwordExpirationEntity.getCreationTime();

      // if user password = password in password expiration table
      // that mean this is new password generated by admin or user
      if (user.getPassword().equals(passwordExpirationEntity.getPassword())) {
        // check if password is in 5 minutes range
        if (runTime.isAfter(creationTime.plusMinutes(5))) {
          throw new UserPasswordException();
        }

        if (passwordExpirationEntity.isNeedsChange()) {
          throw new UserPasswordException(UserPasswordException.PASSWORD_NEED_CHANGED);
        }
      }
    }

    // if not present => continue
  }
}

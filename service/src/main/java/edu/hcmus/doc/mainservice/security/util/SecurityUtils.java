package edu.hcmus.doc.mainservice.security.util;

import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.DOC_BATCH_JOB_USER;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class SecurityUtils {

  private static final int FIVE_MINUTES = 300;

  private static final int ONE_DAY = 86400;

  private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+";

  private SecurityUtils() {
  }

  public static String getCurrentName() {
    return getSecurityAuthentication().getName();
  }

  public static User getCurrentUser() {
    Jwt jwt = (Jwt) getSecurityAuthentication().getPrincipal();

    User currentUser = new User();
    currentUser.setId(getExternalId(jwt.getSubject()));
    currentUser.setUsername(jwt.getClaimAsString("username"));
    currentUser.setEmail(jwt.getClaimAsString("email"));
    currentUser.setFullName(jwt.getClaimAsString("fullName"));

    getSecurityAuthentication().getAuthorities().forEach(authority -> currentUser.setRole(DocSystemRoleEnum.valueOf(authority.getAuthority())));

    return currentUser;
  }

  public static Long getCurrentUserId() {
    if (getSecurityAuthentication().getPrincipal() instanceof Jwt jwt) {
      return getExternalId(jwt.getSubject());
    }

    return 1L;
  }

  public static void setSecurityContextForBatchJob(String batchJobName) {
    Map<String, Object> headers = Map.of(
        "alg", "RS256",
        "typ", "JWT",
        "kid", UUID.randomUUID());

    Map<String, Object> claims = Map.of(
        "sub", DOC_BATCH_JOB_USER,
        "username", DOC_BATCH_JOB_USER,
        "email", DOC_BATCH_JOB_USER,
        "fullName", DOC_BATCH_JOB_USER,
        "realm_access", Map.of("roles", List.of(DOC_BATCH_JOB_USER.value))
    );

    Jwt jwt = new Jwt(
        batchJobName,
        Instant.now(),
        Instant.now().plusSeconds(FIVE_MINUTES),
        headers,
        claims);

    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  public static void setSecurityContextForTesting(User user) {
    Map<String, Object> headers = Map.of(
        "alg", "RS256",
        "typ", "JWT",
        "kid", UUID.randomUUID());

    Map<String, Object> claims = Map.of(
        "sub", "f:" + UUID.randomUUID() + ":" + user.getId(),
        "username", user.getUsername(),
        "realm_access", Map.of("roles", List.of(user.getRole().value))
    );

    Jwt jwt = new Jwt(
        user.getId().toString(),
        Instant.now(),
        Instant.now().plusSeconds(ONE_DAY),
        headers,
        claims);

    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  public static String generateRandomPassword() {
    PasswordGenerator gen = new PasswordGenerator();
    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
    lowerCaseRule.setNumberOfCharacters(1);

    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
    upperCaseRule.setNumberOfCharacters(1);

    CharacterData digitChars = EnglishCharacterData.Digit;
    CharacterRule digitRule = new CharacterRule(digitChars);
    digitRule.setNumberOfCharacters(1);

    CharacterData specialChars = new CharacterData() {
      @Override
      public String getErrorCode() {
        return "ERROR_CODE";
      }

      @Override
      public String getCharacters() {
        return SPECIAL_CHARACTERS;
      }
    };
    CharacterRule splCharRule = new CharacterRule(specialChars);
    splCharRule.setNumberOfCharacters(1);

    return gen.generatePassword(8, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
  }

  private static Authentication getSecurityAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private static Long getExternalId(String keycloakId) {
    return Long.parseLong(keycloakId.substring(keycloakId.lastIndexOf(":") + 1));
  }
}

package edu.hcmus.doc.mainservice.security.util;

import static edu.hcmus.doc.mainservice.model.enums.BatchJobEnum.DOC_BATCH_JOB_USER;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class SecurityUtils {

  private static final int FIVE_MINUTES = 300;

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
        "realm_access", Map.of("roles", List.of(DOC_BATCH_JOB_USER))
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

  private static Authentication getSecurityAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private static Long getExternalId(String keycloakId) {
    return Long.parseLong(keycloakId.substring(keycloakId.lastIndexOf(":") + 1));
  }
}

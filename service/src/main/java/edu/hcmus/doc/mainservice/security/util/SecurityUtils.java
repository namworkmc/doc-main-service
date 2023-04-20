package edu.hcmus.doc.mainservice.security.util;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class SecurityUtils {

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

  private static Authentication getSecurityAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private static Long getExternalId(String keycloakId) {
    return Long.parseLong(keycloakId.substring(keycloakId.lastIndexOf(":") + 1));
  }
}

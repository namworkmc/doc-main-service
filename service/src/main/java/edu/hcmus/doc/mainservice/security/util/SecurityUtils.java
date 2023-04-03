package edu.hcmus.doc.mainservice.security.util;

import edu.hcmus.doc.mainservice.model.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class SecurityUtils {

  public String getCurrentName() {
    return getSecurityAuthentication().getName();
  }

  public User getCurrentUser() {
    Jwt jwt = (Jwt) getSecurityAuthentication().getPrincipal();

    User currentUser = new User();
    currentUser.setId(getExternalId(jwt.getSubject()));
    currentUser.setUsername(jwt.getClaimAsString("username"));
    currentUser.setEmail(jwt.getClaimAsString("email"));
    currentUser.setFullName(jwt.getClaimAsString("fullName"));
    return currentUser;
  }

  private Authentication getSecurityAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private Long getExternalId(String keycloakId) {
    return Long.parseLong(keycloakId.substring(keycloakId.lastIndexOf(":") + 1));
  }
}

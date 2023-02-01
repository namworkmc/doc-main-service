package edu.hcmus.doc.security.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

  private static final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

  public String getCurrentName() {
    return authentication.getName();
  }
}

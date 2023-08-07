package edu.hcmus.doc.mainservice.security.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

  @Test
  void generateRandomPassword() {
    String password = SecurityUtils.generateRandomPassword();
    assertTrue(password.length() >= 8);
  }
}

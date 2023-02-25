package edu.hcmus.doc.mainservice.model.exception;

public class UserRoleNotFoundException extends DocNotFoundException {

  public static final String USER_ROLE_NOT_FOUND = "USER_ROLE.NOT_FOUND";

  public UserRoleNotFoundException(String message) {
    super(message);
  }
}

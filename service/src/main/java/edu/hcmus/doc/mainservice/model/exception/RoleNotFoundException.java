package edu.hcmus.doc.mainservice.model.exception;

public class RoleNotFoundException extends DocNotFoundException {

  public static final String ROLE_NOT_FOUND = "ROLE.NOT_FOUND";

  public RoleNotFoundException(String message) {
    super(message);
  }
}

package edu.hcmus.doc.model.exception;

public class UserNotFoundException extends DocNotFoundException {

  public static final String USER_NOT_FOUND = "USER.NOT_FOUND";

  public UserNotFoundException(String message) {
    super(message);
  }
}

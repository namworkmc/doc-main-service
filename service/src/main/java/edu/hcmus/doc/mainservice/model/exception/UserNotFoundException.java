package edu.hcmus.doc.mainservice.model.exception;

public class UserNotFoundException extends DocNotFoundException {

  public static final String USER_NOT_FOUND = "user.not_found";

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super(USER_NOT_FOUND);
  }
}

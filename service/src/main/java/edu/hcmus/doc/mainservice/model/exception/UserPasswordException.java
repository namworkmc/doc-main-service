package edu.hcmus.doc.mainservice.model.exception;

public class UserPasswordException extends DocBusinessException {

  private static final String USER_PASSWORD_INCORRECT = "user.password.incorrect";

  public static final String PASSWORD_CONFIRMATION_INVALID = "user.password.confirmation.invalid";

  public static final String PASSWORD_NOT_CHANGED = "user.password.not_changed";

  public UserPasswordException() {
    super(USER_PASSWORD_INCORRECT);
  }

  public UserPasswordException(String message) {
    super(message);
  }
}

package edu.hcmus.doc.mainservice.model.exception;

public class DocAuthorizedException extends DocException {

  public static final String USER_INVALID = "user.invalid";
  public static final String REFRESH_TOKEN_INVALID = "REFRESH_TOKEN.INVALID";

  protected DocAuthorizedException(String message) {
    super(message);
  }
}

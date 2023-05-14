package edu.hcmus.doc.mainservice.model.exception;

public class EmailExistedException extends DocExistedException {

  private static final String USER_EMAIL_EXISTED = "user.email.existed";

  public EmailExistedException() {
    super(USER_EMAIL_EXISTED);
  }
}

package edu.hcmus.doc.mainservice.model;

import edu.hcmus.doc.mainservice.model.exception.DocExistedException;

public class EmailExistedException extends DocExistedException {

  private static final String USER_EMAIL_EXISTED = "user.email.existed";

  public EmailExistedException() {
    super(USER_EMAIL_EXISTED);
  }
}

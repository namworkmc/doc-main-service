package edu.hcmus.doc.mainservice.model.exception;

public class UsernameExistedException extends DocExistedException {

  private static final String USERNAME_EXISTED = "user.username.existed";

  public UsernameExistedException() {
    super(USERNAME_EXISTED);
  }
}

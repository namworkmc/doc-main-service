package edu.hcmus.doc.mainservice.model.exception;

public class ExtendRequestNotFoundException extends DocNotFoundException {

  public static final String EXTEND_REQUEST_NOT_FOUND = "extend_request.not_found";

  public ExtendRequestNotFoundException(String message) {
    super(message);
  }

  public ExtendRequestNotFoundException() {
    super(EXTEND_REQUEST_NOT_FOUND);
  }
}

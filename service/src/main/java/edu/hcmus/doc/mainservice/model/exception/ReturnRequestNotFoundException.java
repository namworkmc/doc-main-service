package edu.hcmus.doc.mainservice.model.exception;

public class ReturnRequestNotFoundException extends DocNotFoundException {

  public static final String RETURN_REQUEST_NOT_FOUND = "return_request.not_found";

  public ReturnRequestNotFoundException(String message) {
    super(message);
  }

  public ReturnRequestNotFoundException() {
    super(RETURN_REQUEST_NOT_FOUND);
  }
}

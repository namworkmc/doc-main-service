package edu.hcmus.doc.mainservice.model.exception;

public class ExtensionRequestNotFoundException extends DocNotFoundException {

  public static final String EXTENSION_REQUEST_NOT_FOUND = "extension_request.not_found";

  public ExtensionRequestNotFoundException(String message) {
    super(message);
  }
}

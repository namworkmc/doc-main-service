package edu.hcmus.doc.mainservice.model.exception;

public class ProcessingMethodNotFoundException extends DocNotFoundException {

  public static final String PROCESSING_METHOD_NOT_FOUND = "processing_method.not_found";

  public ProcessingMethodNotFoundException(String message) {
    super(message);
  }

  public ProcessingMethodNotFoundException() {
    super(PROCESSING_METHOD_NOT_FOUND);
  }
}

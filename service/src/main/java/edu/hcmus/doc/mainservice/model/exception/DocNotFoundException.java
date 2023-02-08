package edu.hcmus.doc.mainservice.model.exception;

public abstract class DocNotFoundException extends RuntimeException {

  protected DocNotFoundException(String message) {
    super(message);
  }
}

package edu.hcmus.doc.model.exception;

public abstract class DocNotFoundException extends RuntimeException {

  protected DocNotFoundException(String message) {
    super(message);
  }
}

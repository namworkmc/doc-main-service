package edu.hcmus.doc.mainservice.model.exception;

public abstract class DocExistedException extends RuntimeException {

  protected DocExistedException(String message) {
    super(message);
  }
}

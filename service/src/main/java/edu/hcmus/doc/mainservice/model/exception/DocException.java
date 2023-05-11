package edu.hcmus.doc.mainservice.model.exception;

public class DocException extends RuntimeException {

  public static final String CONCURRENT_UPDATE = "doc.exception.concurrent_update";

  protected DocException(String message) {
    super(message);
  }
}

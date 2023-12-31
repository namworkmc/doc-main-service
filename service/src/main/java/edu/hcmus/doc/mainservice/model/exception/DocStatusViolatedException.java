package edu.hcmus.doc.mainservice.model.exception;

public class DocStatusViolatedException extends DocBusinessException {

  public static final String STATUS_VIOLATED = "doc.status.violated";

  public DocStatusViolatedException(String message) {
    super(message);
  }
}

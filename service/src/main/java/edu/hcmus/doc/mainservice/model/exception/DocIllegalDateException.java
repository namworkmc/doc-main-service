package edu.hcmus.doc.mainservice.model.exception;

public class DocIllegalDateException extends DocNotFoundException {

  public static final String DOC_ILLEGAL_DATE = "doc.illegal_date";

  public DocIllegalDateException(String message) {
    super(message);
  }
}

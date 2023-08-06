package edu.hcmus.doc.mainservice.model.exception;

public class DocMandatoryFields extends DocBusinessException {

  private final String field;

  public DocMandatoryFields(String message) {
    super(message);
    this.field = null;
  }

  public DocMandatoryFields(String field, String message) {
    super(field + ": " + message, message);
    this.field = field;
  }
}

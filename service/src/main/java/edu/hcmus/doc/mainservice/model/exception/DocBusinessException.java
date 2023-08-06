package edu.hcmus.doc.mainservice.model.exception;

import lombok.Getter;

@Getter
public class DocBusinessException extends RuntimeException {

  public static final String DOCUMENT_REQUIRED = "doc.exception.document_required";

  protected final String businessMessage;

  public DocBusinessException(String message) {
    super(message);
    this.businessMessage = message;
  }

  public DocBusinessException(String technicalMessage, String businessMessage) {
    super(technicalMessage);
    this.businessMessage = businessMessage;
  }
}

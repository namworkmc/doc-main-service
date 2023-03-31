package edu.hcmus.doc.mainservice.model.exception;

public class IncomingDocumentNotFoundException extends DocNotFoundException {
  public static final String INCOMING_DOCUMENT_NOT_FOUND = "incoming_document.not_found";

  public IncomingDocumentNotFoundException(String message) {
    super(message);
  }
}

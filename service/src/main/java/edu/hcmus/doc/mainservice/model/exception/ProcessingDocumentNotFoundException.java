package edu.hcmus.doc.mainservice.model.exception;

public class ProcessingDocumentNotFoundException extends DocNotFoundException {

  public static final String PROCESSING_DOCUMENT_NOT_FOUND = "processing_document.not_found";

  public ProcessingDocumentNotFoundException(String message) {
    super(message);
  }

  public ProcessingDocumentNotFoundException() {
    super(PROCESSING_DOCUMENT_NOT_FOUND);
  }
}

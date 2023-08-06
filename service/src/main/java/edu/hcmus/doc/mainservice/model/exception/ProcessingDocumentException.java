package edu.hcmus.doc.mainservice.model.exception;

public class ProcessingDocumentException extends DocBusinessException {

  public static final String ILLEGAL_ROLE = "processing_document.illegal_role";
  public static final String ILLEGAL_STEP = "processing_document.illegal_step";
  public static final String CLOSE_UNPROCESSED_DOCUMENT = "processing_document.close_unprocessed_document";
  public static final String DOCUMENT_ALREADY_CLOSED = "processing_document.document_already_closed";

  public ProcessingDocumentException(String message) {
    super(message);
  }
}

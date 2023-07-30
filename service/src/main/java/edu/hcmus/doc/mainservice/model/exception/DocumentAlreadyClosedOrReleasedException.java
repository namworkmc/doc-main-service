package edu.hcmus.doc.mainservice.model.exception;


public class DocumentAlreadyClosedOrReleasedException extends DocExistedException {

  private static final String DOCUMENT_ALREADY_CLOSED_OR_REALEASED = "transfer_modal.error.document_already_closed_or_released";

  public DocumentAlreadyClosedOrReleasedException() {
    super(DOCUMENT_ALREADY_CLOSED_OR_REALEASED);
  }
}

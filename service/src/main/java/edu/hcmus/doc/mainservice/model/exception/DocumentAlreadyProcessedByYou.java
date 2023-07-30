package edu.hcmus.doc.mainservice.model.exception;

public class DocumentAlreadyProcessedByYou extends DocExistedException {

  private static final String DOCUMENT_ALREADY_PROCESSED_BY_YOU = "transfer_modal.error.document_already_processed_by_you";

  public DocumentAlreadyProcessedByYou() {
    super(DOCUMENT_ALREADY_PROCESSED_BY_YOU);
  }
}

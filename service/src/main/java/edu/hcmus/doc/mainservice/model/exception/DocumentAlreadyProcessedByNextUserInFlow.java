package edu.hcmus.doc.mainservice.model.exception;

public class DocumentAlreadyProcessedByNextUserInFlow extends DocExistedException {

  private static final String DOCUMENT_ALREADY_PROCESSED_BY_NEXT_USER_IN_FLOW = "transfer_modal.error.document_already_processed_by_next_user_in_flow";

  public DocumentAlreadyProcessedByNextUserInFlow() {
    super(DOCUMENT_ALREADY_PROCESSED_BY_NEXT_USER_IN_FLOW);
  }
}

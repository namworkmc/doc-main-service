package edu.hcmus.doc.mainservice.model.exception;

public class LinkedDocumentExistedException extends DocExistedException {

  private static final String LINKED_DOCUMENT_EXISTED = "linked-document.existed";

  public LinkedDocumentExistedException() {
    super(LINKED_DOCUMENT_EXISTED);
  }
}

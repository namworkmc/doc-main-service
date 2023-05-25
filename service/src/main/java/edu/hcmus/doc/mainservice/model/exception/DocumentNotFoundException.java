package edu.hcmus.doc.mainservice.model.exception;

import lombok.Getter;

@Getter
public class DocumentNotFoundException extends DocNotFoundException {

  public static final String DOCUMENT_NOT_FOUND = "DOCUMENT.NOT_FOUND";
  public static final String FOLDER_NOT_FOUND = "FOLDER.NOT_FOUND";
  public static final String INCOMING_DOCUMENT_NOT_FOUND = "incoming_document.not_found";
  public static final String OUTGOING_DOCUMENT_NOT_FOUND = "outgoing_document.not_found";

  public DocumentNotFoundException(String message) {
    super(message);
  }
}

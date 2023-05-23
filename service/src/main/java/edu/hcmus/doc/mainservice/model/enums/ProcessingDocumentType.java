package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessingDocumentType {
  INCOMING_DOCUMENT("INCOMING_DOCUMENT"),
  OUTGOING_DOCUMENT("OUTGOING_DOCUMENT");

  public final String value;
}

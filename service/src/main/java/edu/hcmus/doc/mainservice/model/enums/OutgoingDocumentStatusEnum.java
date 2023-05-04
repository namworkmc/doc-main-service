package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OutgoingDocumentStatusEnum {

  UNPROCESSED("UNPROCESSED"),
  IN_PROGRESS("IN_PROGRESS"),
  WAITING_FOR_OUTGOING_NUMBER("WAITING_FOR_OUTGOING_NUMBER"),
  READY_TO_RELEASE("READY_TO_RELEASE"),
  RELEASED("RELEASED");

  public final String value;
}

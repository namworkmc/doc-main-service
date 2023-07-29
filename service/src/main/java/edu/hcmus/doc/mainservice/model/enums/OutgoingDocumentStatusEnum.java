package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OutgoingDocumentStatusEnum {

  UNPROCESSED("UNPROCESSED"),
  IN_PROGRESS("IN_PROGRESS"),
  RELEASED("RELEASED");

  public final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessingStatus {

  IN_PROGRESS("IN_PROGRESS"),
  CLOSED("CLOSED");

  public final String value;
}

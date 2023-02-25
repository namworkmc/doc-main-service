package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RequestStatus {

  PENDING("PENDING"),

  APPROVED("APPROVED"),

  REJECTED("REJECTED");

  public final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocumentReminderStatusEnum {

  ACTIVE("ACTIVE"),
  CLOSE_TO_EXPIRATION("CLOSE_TO_EXPIRATION"),
  EXPIRED("EXPIRED");

  private final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocSystemRoleEnum {
  DIRECTOR("DIRECTOR"),
  EXPERT("EXPERT"),
  MANAGER("MANAGER"),
  STAFF("STAFF");

  public final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReturnRequestType {
  WITHDRAW("WITHDRAW"),
  SEND_BACK("SEND_BACK");

  public final String value;
}

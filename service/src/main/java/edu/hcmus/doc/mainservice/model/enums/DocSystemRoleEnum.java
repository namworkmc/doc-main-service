package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocSystemRoleEnum {
  GIAM_DOC("GIAM_DOC"),
  CHUYEN_VIEN("CHUYEN_VIEN"),
  TRUONG_PHONG("TRUONG_PHONG"),
  VAN_THU("VAN_THU");

  public final String value;
}

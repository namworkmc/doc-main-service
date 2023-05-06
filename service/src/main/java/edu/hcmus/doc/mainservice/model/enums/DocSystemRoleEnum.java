package edu.hcmus.doc.mainservice.model.enums;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocSystemRoleEnum {
  GIAM_DOC("GIAM_DOC"),
  CHUYEN_VIEN("CHUYEN_VIEN"),
  TRUONG_PHONG("TRUONG_PHONG"),
  VAN_THU("VAN_THU"),
  DOC_ADMIN("DOC_ADMIN");

  public static final List<String> ALL_ROLES_AS_STRING = List.of(GIAM_DOC.value, CHUYEN_VIEN.value, TRUONG_PHONG.value, VAN_THU.value, DOC_ADMIN.value);

  public final String value;
}

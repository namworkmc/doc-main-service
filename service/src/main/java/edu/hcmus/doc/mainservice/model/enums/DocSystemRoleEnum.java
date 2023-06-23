package edu.hcmus.doc.mainservice.model.enums;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocSystemRoleEnum {
  HIEU_TRUONG("HIEU_TRUONG"),
  CHUYEN_VIEN("CHUYEN_VIEN"),
  TRUONG_PHONG("TRUONG_PHONG"),
  VAN_THU("VAN_THU"),
  DOC_ADMIN("DOC_ADMIN");

  public static final List<String> ALL_ROLES_AS_STRING = List.of(HIEU_TRUONG.value, CHUYEN_VIEN.value, TRUONG_PHONG.value, VAN_THU.value, DOC_ADMIN.value);

  public final String value;
}

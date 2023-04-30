package edu.hcmus.doc.mainservice.model.enums;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessMethod {
  BAO_CAO_KET_QUA("Báo cáo kết quả thực hiện"),
  LUU_THAM_KHAO("Lưu tham khảo"),
  SOAN_VAN_BAN("Soạn văn bản trả lời");

  public final String value;

  public static ProcessMethod fromValue(String value) {
    return Stream.of(ProcessMethod.values()).filter(processMethod -> processMethod.value.equals(value))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(
                "No enum constant " + ProcessMethod.class.getCanonicalName() + "." + value));
  }

}

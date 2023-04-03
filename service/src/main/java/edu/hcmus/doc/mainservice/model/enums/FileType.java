package edu.hcmus.doc.mainservice.model.enums;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {
  PDF("application/pdf"),
  PNG("image/png"),
  JPG("image/jpeg");


  public final String value;

  public static FileType fromValue(String value) {
    return Stream.of(FileType.values()).filter(fileType -> fileType.value.equals(value))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(
                "No enum constant " + FileType.class.getCanonicalName() + "." + value));
  }
}

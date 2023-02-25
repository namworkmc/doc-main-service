package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Confidentiality {
  LOW("LOW"),
  MEDIUM("MEDIUM"),
  HIGH("HIGH");
  
  public final String value;
}

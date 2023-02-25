package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessingDocumentRoleEnum {

  APPROVER("APPROVER"),
  REVIEWER("REVIEWER"),
  SUBMITTER("SUBMITTER");

  public final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessingDocumentRoleEnum {

  ASSIGNEE("ASSIGNEE"),
  REPORTER("REPORTER"),
  COLLABORATOR("COLLABORATOR");

  public final String value;
}

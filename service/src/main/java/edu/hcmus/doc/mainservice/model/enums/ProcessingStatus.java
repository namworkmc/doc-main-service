package edu.hcmus.doc.mainservice.model.enums;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessingStatus {

  UNPROCESSED("UNPROCESSED"),
  IN_PROGRESS("IN_PROGRESS"),
  CLOSED("CLOSED");

  public final String value;
  public static final List<ProcessingStatus> PROCESSING_STATUSES = List.of(CLOSED, IN_PROGRESS, UNPROCESSED);
}

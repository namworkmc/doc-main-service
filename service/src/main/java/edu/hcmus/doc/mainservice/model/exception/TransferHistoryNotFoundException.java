package edu.hcmus.doc.mainservice.model.exception;

import lombok.Getter;

@Getter
public class TransferHistoryNotFoundException extends DocNotFoundException {

  public static final String TRANSFER_HISTORY_NOT_FOUND = "TRANSFER_HISTORY.NOT_FOUND";

  public TransferHistoryNotFoundException(String message) {
    super(message);
  }
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransferDocumentType {
  TRANSFER_TO_GIAM_DOC("TRANSFER_TO_GIAM_DOC"),
  TRANSFER_TO_CHUYEN_VIEN("TRANSFER_TO_CHUYEN_VIEN"),
  TRANSFER_TO_VAN_THU("TRANSFER_TO_VAN_THU");

  public final String value;
}

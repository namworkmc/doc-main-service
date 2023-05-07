package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransferDocumentType {
  TRANSFER_TO_GIAM_DOC("TRANSFER_TO_GIAM_DOC"),
  TRANSFER_TO_TRUONG_PHONG("TRANSFER_TO_TRUONG_PHONG"),
  TRANSFER_TO_VAN_THU("TRANSFER_TO_VAN_THU"),
  TRANSFER_TO_CHUYEN_VIEN("TRANSFER_TO_CHUYEN_VIEN");

  public final String value;
}

package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransferDocumentComponent {
  TRANSFER_TO_GIAM_DOC(1),
  TRANSFER_TO_TRUONG_PHONG(2),
  TRANSFER_TO_VAN_THU(3),
  TRANSFER_TO_CHUYEN_VIEN(4);

  public final Integer value;
}

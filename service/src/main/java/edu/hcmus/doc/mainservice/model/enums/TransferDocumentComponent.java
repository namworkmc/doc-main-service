package edu.hcmus.doc.mainservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransferDocumentComponent {
  TRANSFER_TO_GIAM_DOC("DirectorScreenComponent"),
  TRANSFER_TO_TRUONG_PHONG("ManagerScreenComponent"),
  TRANSFER_TO_VAN_THU("SecretaryScreenComponent"),
  TRANSFER_TO_CHUYEN_VIEN("ExpertScreenComponent");

  public final String value;
}

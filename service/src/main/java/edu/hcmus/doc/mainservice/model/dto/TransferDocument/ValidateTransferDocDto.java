package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import lombok.Data;

@Data
public class ValidateTransferDocDto {
  private Boolean isValid;
  private String message;
}

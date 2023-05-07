package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.enums.TransferDocumentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferDocumentMenuConfig {
  private String transferDocumentTypeLabel;
  private String component;
  private String menuLabel;
  private Integer menuKey;
  private TransferDocumentType transferDocumentType;
  private Boolean isTransferToSameLevel;
}

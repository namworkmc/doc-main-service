package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTransferDocumentDetailRequest {
  private Long incomingDocumentId;
  private Long userId;
  private ProcessingDocumentRoleEnum role;
  private Integer step;
}

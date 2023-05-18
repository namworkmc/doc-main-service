package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import java.util.List;
import lombok.Data;

@Data
public class GetTransferDocumentDetailCustomResponse {
  private GetTransferDocumentDetailResponse baseInfo;
  private Long assigneeId;
  private List<Long> collaboratorIds;

}

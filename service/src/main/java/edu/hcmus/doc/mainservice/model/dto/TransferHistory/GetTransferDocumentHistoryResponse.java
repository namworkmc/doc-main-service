package edu.hcmus.doc.mainservice.model.dto.TransferHistory;

import java.util.List;
import lombok.Data;

@Data
public class GetTransferDocumentHistoryResponse {

  private List<TransferHistoryDto> transferHistoryDtos;
}

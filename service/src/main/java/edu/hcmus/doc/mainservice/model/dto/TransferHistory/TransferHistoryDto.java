package edu.hcmus.doc.mainservice.model.dto.TransferHistory;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TransferHistoryDto {
  private Long id;

  private List<Long> documentIds;
  private LocalDate createdDate;
  private LocalDate processingDuration;
  private Boolean isInfiniteProcessingTime = false;
  private Boolean isTransferToSameLevel = false;
  private String processMethod;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
}

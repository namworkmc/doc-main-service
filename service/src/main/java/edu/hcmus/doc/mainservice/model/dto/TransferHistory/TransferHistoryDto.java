package edu.hcmus.doc.mainservice.model.dto.TransferHistory;

import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TransferHistoryDto {
  private Long id;

  private List<Long> documentIds;
  private LocalDateTime createdDate;
  private LocalDate processingDuration;
  private Boolean isInfiniteProcessingTime = false;
  private Boolean isTransferToSameLevel = false;
  private ProcessMethod processMethod;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
}

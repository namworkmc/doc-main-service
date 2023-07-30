package edu.hcmus.doc.mainservice.model.dto.TransferHistory;

import edu.hcmus.doc.mainservice.model.dto.Attachment.DocumentWithAttachmentDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
  private String processingMethod;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private List<DocumentWithAttachmentDto> attachments = new ArrayList<>();
  private LocalTime createdTime;
  private Long returnRequestId;
}

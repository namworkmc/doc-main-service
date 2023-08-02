package edu.hcmus.doc.mainservice.model.dto.TransferHistory;

import edu.hcmus.doc.mainservice.model.dto.Attachment.DocumentWithAttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
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
  private Boolean isRead;
  private String processingMethod;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private List<DocumentWithAttachmentDto> attachments = new ArrayList<>();
  private LocalTime createdTime;
  private ReturnRequestGetDto returnRequest;
  private ProcessingDocumentTypeEnum documentType;
}

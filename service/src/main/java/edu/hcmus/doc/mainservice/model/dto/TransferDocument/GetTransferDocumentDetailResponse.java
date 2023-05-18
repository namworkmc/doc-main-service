package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import lombok.Data;

@Data
public class GetTransferDocumentDetailResponse {

  private Long incomingDocumentId;
  private String incomingNumber;
  private String incomingSummary;
  private Long processingDocumentId;
  private LocalDate transferDate;
  private ProcessingStatus processingStatus;
  private LocalDate processingDuration;
  private Boolean isInfiniteProcessingTime = false;
  private Integer step;
  private ProcessMethod processMethod;
  private Long userId;
  private ProcessingDocumentRoleEnum role;
}

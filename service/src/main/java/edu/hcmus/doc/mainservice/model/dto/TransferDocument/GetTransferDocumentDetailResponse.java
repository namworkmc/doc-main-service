package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import lombok.Data;

@Data
public class GetTransferDocumentDetailResponse {

  private Long documentId;
  private String documentNumber;
  private String summary;
  private Long processingDocumentId;
  private LocalDate transferDate;
  private ProcessingStatus processingStatus;
  private LocalDate processingDuration;
  private Boolean isInfiniteProcessingTime = false;
  private Integer step;
  private String processingMethod;
  private Long userId;
  private ProcessingDocumentRoleEnum role;
}

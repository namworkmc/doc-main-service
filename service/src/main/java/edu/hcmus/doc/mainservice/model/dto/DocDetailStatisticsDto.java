package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocDetailStatisticsDto {
  private Long docId;
  private Long incomingDocId;
  private Long outgoingDocId;
  private LocalDate processingDuration;
  private LocalDate createdDate;
  private ProcessingStatus status;
}

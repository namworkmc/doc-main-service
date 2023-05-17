package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IncomingDocumentStatisticsDto {

  private Integer numberOfUnprocessedDocument;
  private Integer numberOfProcessingDocument;
  private Integer numberOfProcessedDocument;
}

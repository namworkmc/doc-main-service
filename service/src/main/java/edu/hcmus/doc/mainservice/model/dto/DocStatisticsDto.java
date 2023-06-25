package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocStatisticsDto {
  private String userName;
  private Integer numberOfProcessedDocumentOnTime;
  private Integer numberOfProcessedDocumentOverdue;
  private Integer numberOfUnprocessedDocumentUnexpired;
  private Integer numberOfUnprocessedDocumentOverdue;

  public DocStatisticsDto(String userName, Integer numberOfProcessedDocumentOnTime, Integer numberOfProcessedDocumentOverdue, Integer numberOfUnprocessedDocumentUnexpired, Integer numberOfUnprocessedDocumentOverdue) {
    this.userName = userName;
    this.numberOfProcessedDocumentOnTime = numberOfProcessedDocumentOnTime;
    this.numberOfProcessedDocumentOverdue = numberOfProcessedDocumentOverdue;
    this.numberOfUnprocessedDocumentUnexpired = numberOfUnprocessedDocumentUnexpired;
    this.numberOfUnprocessedDocumentOverdue = numberOfUnprocessedDocumentOverdue;
  }
}

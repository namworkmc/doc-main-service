package edu.hcmus.doc.mainservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsWrapperDto {

  private IncomingDocumentStatisticsDto incomingDocumentStatisticsDto;
  private DocumentTypeStatisticsWrapperDto documentTypeStatisticsWrapperDto;
  private int quarter;
  private int year;
}

package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class DocumentTypeStatisticsWrapperDto {

  private List<String> xAxisData;
  private List<DocumentTypeStatisticsDto> seriesData;
}

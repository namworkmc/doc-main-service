package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocListStatisticsDto {
  private Long userId;
  private List<Long> processedDocuments;
  private List<Long> unprocessedDocuments;
}

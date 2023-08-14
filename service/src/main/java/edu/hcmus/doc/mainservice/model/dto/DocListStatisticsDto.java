package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocListStatisticsDto {
  private Map<Long, List<DocDetailStatisticsDto>> receivedDocuments;
  private Map<Long, List<DocDetailStatisticsDto>> transferredDocuments;
}

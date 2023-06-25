package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocStatisticsWrapperDto {
  private List<DocStatisticsDto> docStatisticsDtos;
  private String fromDate;
  private String toDate;
}

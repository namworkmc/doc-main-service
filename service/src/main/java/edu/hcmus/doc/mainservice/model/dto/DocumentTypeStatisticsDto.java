package edu.hcmus.doc.mainservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentTypeStatisticsDto {

  private String name;
  private Integer value;
}

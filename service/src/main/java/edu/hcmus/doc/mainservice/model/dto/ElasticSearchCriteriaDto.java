package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

@Data
public class ElasticSearchCriteriaDto {
  private String incomingNumber;
  private String originalSymbolNumber;
  private String summary;
}

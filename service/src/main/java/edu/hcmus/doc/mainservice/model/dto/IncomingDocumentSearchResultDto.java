package edu.hcmus.doc.mainservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingDocumentSearchResultDto {
  private Long id;
  private String incomingNumber;
  private String originalSymbolNumber;
  private String summary;
}

package edu.hcmus.doc.mainservice.model.dto.ReturnRequest;

import lombok.Data;

@Data
public class ReturnRequestDto {
  private Long id;
  private Long currentProcessingUserId;
  private Long previousProcessingUserId;
  private Long documentId;
  private String reason;
}

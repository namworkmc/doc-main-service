package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MobileNotificationMessageDto {

  private String title;
  private String body;
  private String token;
  private ProcessingDocumentTypeEnum processingDocumentType;
  private Long documentId;
}

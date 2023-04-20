package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DocumentReminderDetailsDto extends DocAbstractDto {

  private String incomingNumber;
  private String summary;
  private LocalDate expirationDate;
  private DocumentReminderStatusEnum status;
  private Long processingDocumentId;
}

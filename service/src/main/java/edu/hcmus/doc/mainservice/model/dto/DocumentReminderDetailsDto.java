package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DocumentReminderDetailsDto extends DocAbstractDto {

  private String documentName;
  private String documentNumber;
  private Long documentId;
  private String summary;
  private LocalDate expirationDate;
  private ProcessingDocumentTypeEnum documentType;
}

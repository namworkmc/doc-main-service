package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import lombok.Data;

@Data
public class DocumentReminderWrapperDto {

  private DocumentReminderDetailsDto documentReminderDetailsDto;
  private DocumentReminderStatusEnum status;
}

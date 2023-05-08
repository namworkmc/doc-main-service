package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

@Data
public class ProcessingDetailsDto {

  private String incomingNumber;
  private Integer step;
  private ProcessingUserDto processingUser;
}

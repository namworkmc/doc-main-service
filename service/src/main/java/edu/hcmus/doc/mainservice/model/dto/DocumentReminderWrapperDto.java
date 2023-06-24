package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class DocumentReminderWrapperDto {

  @JsonProperty("ACTIVE")
  private List<DocumentReminderDetailsDto> active;

  @JsonProperty("CLOSE_TO_EXPIRATION")
  private List<DocumentReminderDetailsDto> closeToExpiration;

  @JsonProperty("EXPIRED")
  private List<DocumentReminderDetailsDto> expired;
}

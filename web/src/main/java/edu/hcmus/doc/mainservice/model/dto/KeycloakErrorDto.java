package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeycloakErrorDto {

  @JsonProperty("error")
  private String error;

  @JsonProperty("error_description")
  private String errorDescription;
}

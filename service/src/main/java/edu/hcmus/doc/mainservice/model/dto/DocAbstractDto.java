package edu.hcmus.doc.mainservice.model.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DocAbstractDto {
  protected Long id;
  protected Long version;
  protected LocalDateTime createdDate;
  protected String createdBy;
}

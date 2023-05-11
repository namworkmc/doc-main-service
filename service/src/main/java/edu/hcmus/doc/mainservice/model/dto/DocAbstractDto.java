package edu.hcmus.doc.mainservice.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class DocAbstractDto {
  protected Long id;
  protected Long version;
  @Nullable
  protected LocalDateTime createdDate;
  @Nullable
  protected String createdBy;
}

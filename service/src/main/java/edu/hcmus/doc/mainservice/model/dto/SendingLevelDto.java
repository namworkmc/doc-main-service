package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.SendingLevel} entity
 */
@Data
public class SendingLevelDto extends DocAbstractDto {
  private String level;
}
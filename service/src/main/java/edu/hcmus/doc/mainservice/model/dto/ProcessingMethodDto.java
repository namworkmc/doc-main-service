package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.ProcessingMethod} entity
 */
@Data
public class ProcessingMethodDto extends DocAbstractDto {

  private String name;
}

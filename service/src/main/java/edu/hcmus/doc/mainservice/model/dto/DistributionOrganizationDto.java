package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.DistributionOrganization} entity
 */
@Data
public class DistributionOrganizationDto extends DocAbstractDto {
  private String name;
  private String symbol;
}
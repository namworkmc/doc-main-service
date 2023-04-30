package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.Department} entity
 */
@Data
public class DepartmentDto extends DocAbstractDto {

  private String departmentName;
}

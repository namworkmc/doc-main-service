package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.Department} entity
 */
@Data
public class DepartmentDto extends DocAbstractDto {

  private String departmentName;

  @Nullable
  private TruongPhongDto truongPhong;

  @Nullable
  private String description;
}

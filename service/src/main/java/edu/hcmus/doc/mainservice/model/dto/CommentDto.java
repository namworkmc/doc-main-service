package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.Comment} entity
 */
@Data
public class CommentDto extends DocAbstractDto {

  private String content;
}

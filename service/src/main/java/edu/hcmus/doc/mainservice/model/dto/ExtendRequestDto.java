package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * A DTO for the {@link ExtendRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendRequestDto extends DocAbstractDto {

  private Long processingUserId;

  private LocalDate oldDeadline;

  private ExtendRequestStatus status;

  private Long documentToExtendId;
  @FutureOrPresent
  private LocalDate newDeadline;

  private String reason;

  @Nullable
  private Long validatorId;

  public ExtendRequestDto(LocalDate oldDeadline) {
    this.oldDeadline = oldDeadline;
  }
}

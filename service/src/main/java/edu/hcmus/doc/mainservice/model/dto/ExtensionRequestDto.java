package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.ExtensionRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionRequestDto extends DocAbstractDto {

  @FutureOrPresent
  private LocalDate extendedUntil;
  private String reason;
  private ExtensionRequestStatus status;
  private Long validatorId;
  private LocalDate oldExpiredDate;
  private Long processingUserId;
}

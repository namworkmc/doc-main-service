package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import edu.hcmus.doc.mainservice.util.validator.annotation.StringDateFutureOrPresent;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentType;
import java.util.List;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class TransferDocDto {

  @Nullable
  private List<Long> documentIds;

  @Nullable
  private String summary;

  @Nullable
  private Long reporterId;

  @Nullable
  private Long assigneeId;

  @Nullable
  private List<Long> collaboratorIds;

  @Nullable
  @StringDateFutureOrPresent(message = "transfer_modal.form.processing_time_must_be_future")
  private String processingTime;

  @Nullable
  private Boolean isInfiniteProcessingTime;

  @Nullable
  private String processingMethod;

  private TransferDocumentType transferDocumentType;

  private Boolean isTransferToSameLevel;
}

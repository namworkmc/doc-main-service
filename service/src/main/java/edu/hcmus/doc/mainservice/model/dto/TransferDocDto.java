package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
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
  private String processingTime;
  @Nullable
  private Boolean isInfiniteProcessingTime;
  @Nullable
  private ProcessMethod processMethod;
  private TransferDocumentType transferDocumentType;
  private Boolean isTransferToSameLevel;
}

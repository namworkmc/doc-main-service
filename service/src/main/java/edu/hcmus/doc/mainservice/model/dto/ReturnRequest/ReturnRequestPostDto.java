package edu.hcmus.doc.mainservice.model.dto.ReturnRequest;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ReturnRequestType;
import java.util.List;
import org.springframework.lang.Nullable;
import lombok.Data;

@Data
public class ReturnRequestPostDto {
  private Long currentProcessingUserId;
  @Nullable
  private Long previousProcessingUserId;
  private List<Long> documentIds;
  private ProcessingDocumentTypeEnum documentType;
  private String reason;
  private Integer step;
  private ReturnRequestType returnRequestType;
}

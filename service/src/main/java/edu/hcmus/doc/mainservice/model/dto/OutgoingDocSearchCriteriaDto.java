package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import java.time.LocalDate;
import lombok.Data;

@Data
public class OutgoingDocSearchCriteriaDto {

  private String outgoingNumber;
  private String originalSymbolNumber;
  private Long documentTypeId;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate releaseDateFrom;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate releaseDateTo;
  private String summary;
  private OutgoingDocumentStatusEnum status;
  private String documentName;
}

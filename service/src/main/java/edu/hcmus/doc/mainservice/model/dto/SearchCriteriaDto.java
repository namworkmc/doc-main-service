package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SearchCriteriaDto {

  private String incomingNumber;
  private String originalSymbolNumber;
  private Long documentTypeId;
  private Long distributionOrgId;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate arrivingDateFrom;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate arrivingDateTo;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate processingDurationFrom;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate processingDurationTo;
  private String summary;
  private ProcessingStatus status;
  private String documentName;
}

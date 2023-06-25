package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DocStatisticsSearchCriteriaDto {
  private List<Long> expertIds;
  private String docType;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate fromDate;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate toDate;
}

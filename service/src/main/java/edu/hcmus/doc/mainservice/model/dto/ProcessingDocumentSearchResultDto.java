package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;
import lombok.Data;

@Data
public class ProcessingDocumentSearchResultDto {
  private List<ProcessingDocument> processingDocuments;
  private long totalElements;
  private long totalPages;
}

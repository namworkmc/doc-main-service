package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;

public interface ProcessingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchProcessingDocuments(SearchCriteriaDto searchCriteriaDto, long offset, long limit);
}

package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProcessingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchProcessingDocuments(SearchCriteriaDto searchCriteriaDto, long offset, long limit);

  ProcessingDocumentSearchResultDto searchProcessingDocumentsByElasticSearch(ElasticSearchCriteriaDto elasticSearchCriteriaDto, long offset, long limit)
      throws ExecutionException, InterruptedException;
}
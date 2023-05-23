package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailCustomResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProcessingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchProcessingDocuments(SearchCriteriaDto searchCriteriaDto, long offset, long limit);

  ProcessingDocumentSearchResultDto searchProcessingDocumentsByElasticSearch(ElasticSearchCriteriaDto elasticSearchCriteriaDto, long offset, long limit)
      throws ExecutionException, InterruptedException;

  Boolean isUserWorkingOnDocumentWithSpecificRole(GetTransferDocumentDetailRequest request);

  Boolean isUserWorkingOnOutgoingDocumentWithSpecificRole(GetTransferDocumentDetailRequest request);

  ValidateTransferDocDto validateTransferDocument(TransferDocDto transferDocDto);

  ValidateTransferDocDto validateTransferOutgoingDocument(TransferDocDto transferDocDto);

  GetTransferDocumentDetailCustomResponse getTransferDocumentDetail(GetTransferDocumentDetailRequest request, ProcessingDocumentType processingDocumentType);

  Integer getCurrentStep(Long documentId);

  ProcessingStatus getProcessingStatus(Long documentId);
}

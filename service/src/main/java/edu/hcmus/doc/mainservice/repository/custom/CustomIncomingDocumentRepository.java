package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomIncomingDocumentRepository {

  Long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument getIncomingDocumentById(Long id);

  IncomingDocument getIncomingDocumentByIdForStatistic(Long id);

  List<IncomingDocument> getIncomingDocumentsByIds(List<Long> ids);

  Map<String, Set<Long>> getQuarterProcessingStatisticsByUserId(Long userId);

  Map<String, Set<Long>> getQuarterProcessingDocumentTypeStatisticsByUserId(Long userId);

  List<IncomingDocument> getDocumentsLinkedToOutgoingDocument(Long sourceDocumentId);
}

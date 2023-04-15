package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import java.util.List;

public interface CustomIncomingDocumentRepository {

  Long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument getIncomingDocumentById(Long id);

  List<IncomingDocument> getIncomingDocumentsByIds(List<Long> ids);
}

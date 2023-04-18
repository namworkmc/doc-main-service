package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.util.List;

public interface CustomProcessingDocumentRepository
    extends DocAbstractSearchRepository<ProcessingDocument> {

  List<ProcessingDocument> findProcessingDocumentsByElasticSearchResult(
      List<IncomingDocumentSearchResultDto> incomingDocumentSearchResultDtoList, long offset,
      long limit);

  List<ProcessingDocument> findAllByIds(List<Long> ids);
}

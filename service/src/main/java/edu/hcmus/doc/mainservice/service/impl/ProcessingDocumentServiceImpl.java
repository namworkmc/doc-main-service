package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingDocumentServiceImpl implements ProcessingDocumentService {

  private final ProcessingDocumentRepository processingDocumentRepository;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return processingDocumentRepository.getTotalPages(searchCriteriaDto, limit);
  }

  @Override
  public List<ProcessingDocument> searchProcessingDocuments(
      SearchCriteriaDto searchCriteriaDto,
      long offset,
      long limit
  ) {
    return processingDocumentRepository.searchByCriteria(searchCriteriaDto, offset, limit);
  }
}

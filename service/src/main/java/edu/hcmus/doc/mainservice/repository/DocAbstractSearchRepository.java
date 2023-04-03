package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import java.util.List;

public interface DocAbstractSearchRepository<T> {

  long getTotalElements(SearchCriteriaDto criteria);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<T> searchByCriteria(SearchCriteriaDto criteria, long offset, long limit);
}

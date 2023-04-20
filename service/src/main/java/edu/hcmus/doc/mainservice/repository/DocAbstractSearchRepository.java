package edu.hcmus.doc.mainservice.repository;

import java.util.List;

public interface DocAbstractSearchRepository<T, C> {

  long getTotalElements(C criteria);

  long getTotalPages(C criteria, long limit);

  List<T> searchByCriteria(C criteria, long offset, long limit);
}

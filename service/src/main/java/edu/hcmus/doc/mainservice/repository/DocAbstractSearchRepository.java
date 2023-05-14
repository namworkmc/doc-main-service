package edu.hcmus.doc.mainservice.repository;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;

public interface DocAbstractSearchRepository<R, C> {

  long getTotalElements(C criteria);

  long getTotalPages(C criteria, long limit);

  List<R> searchByCriteria(C criteria, long offset, long limit);

  JPAQuery<R> buildSearchQuery(C criteria);
}

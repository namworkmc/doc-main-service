package edu.hcmus.doc.mainservice.repository;

import com.querydsl.jpa.impl.JPAQuery;

public interface DocAbstractPagination<R, O, L> {

  JPAQuery<R> buildPaginationQuery(O o, L l);
}

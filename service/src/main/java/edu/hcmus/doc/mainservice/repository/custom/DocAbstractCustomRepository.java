package edu.hcmus.doc.mainservice.repository.custom;

import static edu.hcmus.doc.mainservice.model.entity.DocAbstractVersionEntity.DELETED_FILTER;
import static edu.hcmus.doc.mainservice.model.entity.DocAbstractVersionEntity.IS_DELETED_PARAM;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;

public abstract class DocAbstractCustomRepository<T> {

  @PersistenceContext
  protected EntityManager em;

  protected JPAQuery<T> selectFrom(EntityPath<?>... entity) {
    try (Session session = em.unwrap(Session.class)) {
      session.enableFilter(DELETED_FILTER).setParameter(IS_DELETED_PARAM, false);
      return new JPAQuery<T>(em).from(entity);
    }
  }

  protected <S> JPAQuery<S> selectFrom(EntityPath<S> entity) {
    try (Session session = em.unwrap(Session.class)) {
      session.enableFilter(DELETED_FILTER).setParameter(IS_DELETED_PARAM, false);
      return new JPAQuery<S>(em).from(entity);
    }
  }

  protected JPAQuery<Tuple> select(Expression<?>... entity) {
    return new JPAQuery<Tuple>(em).select(entity);
  }
}

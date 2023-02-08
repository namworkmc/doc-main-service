package edu.hcmus.doc.mainservice.repository.custom;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class DocAbstractCustomRepository<T> {

  @PersistenceContext
  protected EntityManager em;

  public JPAQuery<T> selectFrom(EntityPath<?>... entity) {
    return new JPAQuery<T>(em).from(entity);
  }
}

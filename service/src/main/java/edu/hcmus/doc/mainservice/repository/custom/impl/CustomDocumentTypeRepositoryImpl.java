package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocumentTypeRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomDocumentTypeRepositoryImpl
    extends DocAbstractCustomRepository<DocumentType>
    implements CustomDocumentTypeRepository {

  @Override
  public long getTotalElements(DocumentTypeSearchCriteria criteria) {
    return buildSearchQuery(criteria)
        .select(QDocumentType.documentType.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(DocumentTypeSearchCriteria criteria, long limit) {
    return 0;
  }

  @Override
  public List<DocumentType> searchByCriteria(DocumentTypeSearchCriteria criteria, long offset, long limit) {
    return buildSearchQuery(criteria)
        .orderBy(QDocumentType.documentType.type.asc())
        .orderBy(QDocumentType.documentType.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch();
  }

  @Override
  public JPAQuery<DocumentType> buildSearchQuery(DocumentTypeSearchCriteria criteria) {
    return selectFrom(QDocumentType.documentType);
  }
}

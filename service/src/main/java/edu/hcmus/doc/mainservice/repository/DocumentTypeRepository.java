package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocumentTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends
    JpaRepository<DocumentType, Long>,
    QuerydslPredicateExecutor<DocumentType>,
    CustomDocumentTypeRepository {

}

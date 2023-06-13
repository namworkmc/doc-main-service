package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.LinkedDocument;
import edu.hcmus.doc.mainservice.repository.custom.CustomLinkedDocumentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedDocumentRepository
        extends JpaRepository<LinkedDocument, Long>,
        QuerydslPredicateExecutor<LinkedDocument>,
        CustomLinkedDocumentRepository {

}
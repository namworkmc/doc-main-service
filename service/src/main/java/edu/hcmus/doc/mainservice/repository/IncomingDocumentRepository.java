package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.repository.custom.CustomIncomingDocumentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingDocumentRepository
    extends JpaRepository<IncomingDocument, Long>,
    QuerydslPredicateExecutor<IncomingDocument>,
    CustomIncomingDocumentRepository {

}
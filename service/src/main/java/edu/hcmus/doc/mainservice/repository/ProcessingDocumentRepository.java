package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingDocumentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingDocumentRepository
    extends
    JpaRepository<ProcessingDocument, Long>,
    CustomProcessingDocumentRepository {
}

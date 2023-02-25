package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingDocumentRepository extends JpaRepository<OutgoingDocument, Long> {

}
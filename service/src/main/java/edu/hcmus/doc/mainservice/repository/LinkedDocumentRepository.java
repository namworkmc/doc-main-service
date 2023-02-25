package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.LinkedDocument;
import edu.hcmus.doc.mainservice.model.entity.pk.LinkedDocumentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedDocumentRepository extends JpaRepository<LinkedDocument, LinkedDocumentPK> {

}
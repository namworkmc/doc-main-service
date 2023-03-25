package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

}

package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomOutgoingDocumentRepository;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingDocumentRepository
        extends JpaRepository<OutgoingDocument, Long>,
        QuerydslPredicateExecutor<OutgoingDocument>,
        CustomOutgoingDocumentRepository {

  // update status of outgoing document by id
  @Modifying
  @Transactional // Add this annotation to enable transaction support for the update operation
  @Query("UPDATE OutgoingDocument o SET o.status = ?2 WHERE o.id = ?1")
  void updateStatusById(Long id, OutgoingDocumentStatusEnum status);
}
package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.repository.custom.CustomTransferHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferHistoryRepository extends
    JpaRepository<TransferHistory, Long>, CustomTransferHistoryRepository {

  // find all by sender id or receiver id
  @Query("SELECT th FROM TransferHistory th WHERE th.sender.id = ?1 OR th.receiver.id = ?1 ORDER BY th.createdDate DESC")
  Page<TransferHistory> findAllByUserId(Long userId, Pageable pageable);
}
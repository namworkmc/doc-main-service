package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.repository.custom.CustomReturnRequestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long>,
    QuerydslPredicateExecutor<ReturnRequest>,
    CustomReturnRequestRepository {


}
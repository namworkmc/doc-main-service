package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.repository.custom.CustomExtendRequestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendRequestRepository
    extends JpaRepository<ExtendRequest, Long>,
    QuerydslPredicateExecutor<ExtendRequest>,
    CustomExtendRequestRepository {

}

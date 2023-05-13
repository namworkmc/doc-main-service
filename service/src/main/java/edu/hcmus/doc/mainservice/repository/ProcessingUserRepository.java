package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingUserRepository extends JpaRepository<ProcessingUser, Long>,
    QuerydslPredicateExecutor<ProcessingUser>,
    CustomProcessingUserRepository {

}
package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingMethodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingMethodRepository extends JpaRepository<ProcessingMethod, Long>,
    QuerydslPredicateExecutor<ProcessingMethod>,
    CustomProcessingMethodRepository {

}
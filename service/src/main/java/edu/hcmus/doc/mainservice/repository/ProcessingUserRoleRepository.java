package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingUserRoleRepository extends
    JpaRepository<ProcessingUserRole, Long>,
    QuerydslPredicateExecutor<ProcessingUserRole>,
    CustomProcessingUserRoleRepository {

}

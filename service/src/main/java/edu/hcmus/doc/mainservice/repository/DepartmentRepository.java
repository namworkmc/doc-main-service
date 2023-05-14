package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.repository.custom.CustomDepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository
    extends JpaRepository<Department, Long>,
    QuerydslPredicateExecutor<Department>,
    CustomDepartmentRepository {

}

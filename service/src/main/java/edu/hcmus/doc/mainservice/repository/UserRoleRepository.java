package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.entity.pk.UserRolePK;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends
    JpaRepository<UserRole, UserRolePK>,
    QuerydslPredicateExecutor<UserRole>,
    CustomUserRoleRepository {

}

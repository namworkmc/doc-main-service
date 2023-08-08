package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends
    JpaRepository<User, Long>,
    QuerydslPredicateExecutor<User>,
    CustomUserRepository {

  Optional<User> findUserByUsername(String username);

  boolean existsByDepartmentIdIn(Set<Long> departmentIds);
}

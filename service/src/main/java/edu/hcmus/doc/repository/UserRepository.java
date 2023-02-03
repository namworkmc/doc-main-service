package edu.hcmus.doc.repository;

import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends
    JpaRepository<User, Long>,
    QuerydslPredicateExecutor<User>,
    CustomUserRepository {

}

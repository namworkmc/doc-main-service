package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.PasswordExpiration;
import edu.hcmus.doc.mainservice.repository.custom.CustomPasswordExpirationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordExpirationRepository extends JpaRepository<PasswordExpiration, Long>,
    QuerydslPredicateExecutor<PasswordExpiration>,
    CustomPasswordExpirationRepository {

}

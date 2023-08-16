package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.repository.custom.CustomDistributionOrganizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionOrganizationRepository extends
    JpaRepository<DistributionOrganization, Long>, QuerydslPredicateExecutor<DistributionOrganization>,
    CustomDistributionOrganizationRepository {

}
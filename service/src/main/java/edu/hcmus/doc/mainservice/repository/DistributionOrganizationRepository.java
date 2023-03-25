package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionOrganizationRepository extends
    JpaRepository<DistributionOrganization, Long> {
}
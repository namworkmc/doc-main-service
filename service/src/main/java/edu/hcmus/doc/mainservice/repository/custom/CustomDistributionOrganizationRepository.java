package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;

public interface CustomDistributionOrganizationRepository
    extends DocAbstractSearchRepository<DistributionOrganization, DistributionOrganizationSearchCriteria> {
}

package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import java.util.List;

public interface DistributionOrganizationService extends SearchService<DistributionOrganizationDto, DistributionOrganizationSearchCriteria>  {

  List<DistributionOrganization> findAll();

  DistributionOrganization findById(Long id);

  Long saveDistributionOrganization(DistributionOrganization distributionOrganization);

}

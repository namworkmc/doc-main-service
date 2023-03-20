package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import java.util.List;

public interface DistributionOrganizationService {

  List<DistributionOrganization> findDistributionOrganizations();
}

package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.repository.DistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DistributionOrganizationServiceImpl implements DistributionOrganizationService {

  private final DistributionOrganizationRepository distributionOrganizationRepository;

  @Override
  public List<DistributionOrganization> findDistributionOrganizations() {
    return distributionOrganizationRepository.findAll();
  }
}

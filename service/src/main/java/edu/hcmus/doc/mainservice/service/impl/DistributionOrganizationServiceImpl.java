package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.repository.DistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DistributionOrganizationServiceImpl implements DistributionOrganizationService {
    private final DistributionOrganizationRepository distributionOrganizationRepository;

    public DistributionOrganizationServiceImpl(DistributionOrganizationRepository distributionOrganizationRepository) {
        this.distributionOrganizationRepository = distributionOrganizationRepository;
    }

    @Override
    public List<DistributionOrganization> findAll() {
        return distributionOrganizationRepository.findAll();
    }

    @Override
    public DistributionOrganization findById(Long id) {
        return distributionOrganizationRepository.findById(id).orElse(null);
    }
}

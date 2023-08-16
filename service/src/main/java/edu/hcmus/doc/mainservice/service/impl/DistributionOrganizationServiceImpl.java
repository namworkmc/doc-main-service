package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.repository.DistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import edu.hcmus.doc.mainservice.util.mapper.DistributionOrganizationMapper;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DistributionOrganizationServiceImpl implements DistributionOrganizationService {

    private final DistributionOrganizationRepository distributionOrganizationRepository;
    private final DistributionOrganizationMapper distributionOrganizationMapper;
    private final PaginationMapper paginationMapper;


    @Override
    public List<DistributionOrganization> findAll() {
        return distributionOrganizationRepository.findAll();
    }

    @Override
    public DistributionOrganization findById(Long id) {
        return distributionOrganizationRepository.findById(id).orElse(null);
    }

    @Override
    public Long saveDistributionOrganization(DistributionOrganization distributionOrganization) {
        return distributionOrganizationRepository.save(distributionOrganization).getId();
    }

    @Override
    public DocPaginationDto<DistributionOrganizationDto> search(
        DistributionOrganizationSearchCriteria criteria, int page, int pageSize) {
        long totalElements = distributionOrganizationRepository.getTotalElements(criteria);
        long totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
        List<DistributionOrganizationDto> distributionOrganizationDtoList = distributionOrganizationRepository
            .searchByCriteria(criteria, page, pageSize)
            .stream()
            .map(distributionOrganizationMapper::toDto)
            .toList();

        return paginationMapper.toDto(distributionOrganizationDtoList, totalElements, totalPages);
    }
}

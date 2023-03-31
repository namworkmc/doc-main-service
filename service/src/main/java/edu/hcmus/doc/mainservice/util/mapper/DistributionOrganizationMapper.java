package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistributionOrganizationMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source="symbol", target="symbol")
    DistributionOrganizationDto toDto(DistributionOrganization distributionOrganization);
}

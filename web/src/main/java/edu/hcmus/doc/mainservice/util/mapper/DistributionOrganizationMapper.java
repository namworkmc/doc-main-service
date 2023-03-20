package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistributionOrganizationMapper {

  DistributionOrganizationDto toDto(DistributionOrganization entity);
}

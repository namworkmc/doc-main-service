package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/distribution-organizations")
public class DistributionOrganizationController extends DocAbstractController {

    private final DistributionOrganizationService distributionOrganizationService;

    @GetMapping
    public List<DistributionOrganizationDto> getAll() {
        return distributionOrganizationService.findAll().stream()
                .map(distributionOrganizationMapper::toDto)
                .toList();
    }
}

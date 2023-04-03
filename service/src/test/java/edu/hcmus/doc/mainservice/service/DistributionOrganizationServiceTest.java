package edu.hcmus.doc.mainservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.service.impl.DistributionOrganizationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistributionOrganizationServiceTest extends AbstractServiceTest {

  private DistributionOrganizationService distributionOrganizationService;

  @BeforeEach
  void setUp() {
    this.distributionOrganizationService
        = new DistributionOrganizationServiceImpl(distributionOrganizationRepository);
  }

  @Test
  void findDistributionOrganizations() {
    // Given
    DistributionOrganization distributionOrganization1 = new DistributionOrganization();
    distributionOrganization1.setId(1L);

    DistributionOrganization distributionOrganization2 = new DistributionOrganization();
    distributionOrganization2.setId(2L);

    List<DistributionOrganization> distributionOrganizations = new ArrayList<>();
    distributionOrganizations.add(distributionOrganization1);
    distributionOrganizations.add(distributionOrganization2);

    // When
    when(distributionOrganizationRepository.findAll()).thenReturn(distributionOrganizations);
    List<DistributionOrganization> actual = distributionOrganizationService.findAll();

    // Then
    verify(distributionOrganizationRepository).findAll();

    assertThat(actual).isEqualTo(distributionOrganizations);
  }
}

package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import java.util.List;
import org.junit.jupiter.api.Test;

class DistributionOrganizationRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllDistributionOrganizations() {
    // Given
    // When
    List<DistributionOrganization> actual = distributionOrganizationRepository.findAll();

    // Then
    assertThat(actual).isNotEmpty();
  }
}
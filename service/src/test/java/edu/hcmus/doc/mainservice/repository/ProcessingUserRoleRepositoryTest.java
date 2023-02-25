package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingUserRoleRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingUserRoles() {
    // Given
    // When
    List<ProcessingUserRole> actual = processingUserRoleRepository.findAll();

    // Then
    Assertions.assertThat(actual).isNotEmpty();
  }
}
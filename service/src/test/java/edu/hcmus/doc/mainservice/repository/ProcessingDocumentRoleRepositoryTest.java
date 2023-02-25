package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocumentRole;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingDocumentRoleRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingDocumentRoles() {
    // Given
    // When
    List<ProcessingDocumentRole> processingDocumentRoles = processingDocumentRoleRepository.findAll();

    // Then
    Assertions.assertThat(processingDocumentRoles).isNotEmpty();
  }
}
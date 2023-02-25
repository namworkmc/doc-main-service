package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExtensionRequestRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllExtensionRequests() {
    // Given
    // When
    List<ExtensionRequest> extensionRequests = extensionRequestRepository.findAll();

    // Then
    Assertions.assertThat(extensionRequests).isNotEmpty();
  }
}
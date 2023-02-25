package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.LinkedDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LinkedDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllLinkedDocuments() {
    // Given
    // When
    List<LinkedDocument> linkedDocuments = linkedDocumentRepository.findAll();

    // Then
    Assertions.assertThat(linkedDocuments).isNotEmpty();
  }
}
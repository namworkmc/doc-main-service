package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingDocuments() {
    // Given
    // When
    List<ProcessingDocument> processingDocuments = processingDocumentRepository.findAll();

    // Then
    Assertions.assertThat(processingDocuments).isNotEmpty();
  }
}
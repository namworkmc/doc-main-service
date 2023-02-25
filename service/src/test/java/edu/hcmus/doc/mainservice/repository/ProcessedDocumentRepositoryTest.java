package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.ProcessedDocument;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProcessedDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessedDocuments() {
    // Given
    // When
    List<ProcessedDocument> actual = processedDocumentRepository.findAll();

    // Then
    assertThat(actual).isNotEmpty();
  }
}
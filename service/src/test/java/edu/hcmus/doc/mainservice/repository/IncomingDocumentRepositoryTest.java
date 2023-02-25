package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class IncomingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllIncomingDocuments() {
    // Given
    // When
    List<IncomingDocument> actual = incomingDocumentRepository.findAll();

    // Then
    Assertions.assertThat(actual).isNotEmpty();
  }
}
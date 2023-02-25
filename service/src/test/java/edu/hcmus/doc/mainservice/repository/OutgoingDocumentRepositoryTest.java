package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OutgoingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void getAllOutgoingDocuments() {
    // Given
    // When
    List<OutgoingDocument> outgoingDocuments = outgoingDocumentRepository.findAll();

    // Then
    Assertions.assertThat(outgoingDocuments).isNotEmpty();
  }
}
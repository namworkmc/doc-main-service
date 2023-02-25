package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import java.util.List;
import org.junit.jupiter.api.Test;

class DocumentTypeRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllDocumentTypes() {
    // Given
    // When
    List<DocumentType> actual = documentTypeRepository.findAll();
    // Then
    assertThat(actual).isNotEmpty();
  }
}
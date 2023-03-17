package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testSearchByCriteria() {
    // Given
    SearchCriteriaDto query = new SearchCriteriaDto();
    long offset = 0;
    long limit = 3;

    // When
    List<ProcessingDocument> processingDocuments =
        processingDocumentRepository.searchByCriteria(
            query,
            offset,
            limit
        );

    // Then
    Assertions.assertThat(processingDocuments).isNotEmpty();
  }
}

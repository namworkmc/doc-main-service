package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "user1", roles = {"VAN_THU"})
class ProcessingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  @Ignore
  void testGetTotalElements() {
    // Given
    setLoginUser("user1", DocSystemRoleEnum.VAN_THU, true);
    SearchCriteriaDto query = new SearchCriteriaDto();

    // When
    Long totalElements = processingDocumentRepository.getTotalElements(query);

    // Then
    Assertions.assertThat(totalElements).isPositive();
  }

  @Test
  void testGetTotalPages() {
    // Given
    setLoginUser("user1", DocSystemRoleEnum.VAN_THU, true);
    SearchCriteriaDto query = new SearchCriteriaDto();
    long limit = 3;

    // When
    long totalPages = processingDocumentRepository.getTotalPages(query, limit);

    // Then
    Assertions.assertThat(totalPages).isPositive();
  }

  @Test
  void testSearchByCriteria() {
    // Given
    setLoginUser("user1", DocSystemRoleEnum.VAN_THU, true);
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

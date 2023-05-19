package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReturnRequestRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllReturnRequests() {
    // Given
    // When
    List<ReturnRequest> actual = returnRequestRepository.findAll();

    // Then
    Assertions.assertThat(actual).isNotEmpty();
  }
}

package edu.hcmus.doc.mainservice.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReturnRequestRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllReturnRequests() {
    // Given
    // When
    var actual = returnRequestRepository.findAll();

    // Then
    Assertions.assertThat(actual).isNotEmpty();
  }
}
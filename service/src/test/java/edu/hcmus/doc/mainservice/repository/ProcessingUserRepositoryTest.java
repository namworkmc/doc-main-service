package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingUserRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingUsers() {
    // Given
    // When
    List<ProcessingUser> processingUsers = processingUserRepository.findAll();

    // Then
    Assertions.assertThat(processingUsers).isNotEmpty();
  }
}
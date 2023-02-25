package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.SendingLevel;
import java.util.List;
import org.junit.jupiter.api.Test;

class SendingLevelRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllSendingLevels() {
    // Given
    // When
    List<SendingLevel> actual = sendingLevelRepository.findAll();

    // Then
    assertThat(actual).isNotEmpty();
  }
}
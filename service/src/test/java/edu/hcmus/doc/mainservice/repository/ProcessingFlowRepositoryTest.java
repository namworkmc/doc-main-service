package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingFlow;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingFlowRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingFlows() {
    // Given
    // When
    List<ProcessingFlow> actual = processingFlowRepository.findAll();

    // Then
    Assertions.assertThat(actual).isNotEmpty();
  }
}
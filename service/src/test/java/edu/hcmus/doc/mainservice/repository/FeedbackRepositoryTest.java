package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.Feedback;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FeedbackRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllFeedbacks() {
    // Given
    // When
    List<Feedback> feedbacks = feedbackRepository.findAll();

    // Then
    Assertions.assertThat(feedbacks).isNotEmpty();
  }
}
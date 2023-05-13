package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import java.util.List;

public interface CustomProcessingUserRepository {
  List<ProcessingUser> findAllByUserIdAndProcessingDocumentId(Long userId, Long processingDocumentId);
}

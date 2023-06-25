package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomProcessingUserRepository {

  List<ProcessingUser> findAllByUserIdAndProcessingDocumentId(Long userId, Long processingDocumentId);

  boolean isProcessAtStep(Long incomingDocumentId, int step);

  Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId);

  List<ProcessingUser> findByUserIdAndProcessingDocumentIdWithRole(Long userId, Long processingDocumentId);
}

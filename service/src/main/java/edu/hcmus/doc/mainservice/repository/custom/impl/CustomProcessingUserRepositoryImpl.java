package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomProcessingUserRepositoryImpl extends DocAbstractCustomRepository<ProcessingUser>
    implements CustomProcessingUserRepository {

  @Override
  public List<ProcessingUser> findAllByUserIdAndProcessingDocumentId(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId)))
        .fetch();
  }

  @Override
  public boolean isProcessAtStep(Long incomingDocumentId, int step) {
    return selectFrom(processingUser)
        .select(processingUser.id)
        .where(processingUser.processingDocument.incomingDoc.id.eq(incomingDocumentId)
            .and(processingUser.step.eq(step)))
        .fetchFirst() != null;
  }
}

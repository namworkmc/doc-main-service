package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.ASSIGNEE;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CustomProcessingUserRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingUser>
    implements CustomProcessingUserRepository {

  private static final QProcessingUser qProcessingUser = QProcessingUser.processingUser;

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

  @Override
  public Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId) {
    QProcessingDocument qProcessingDocument = new QProcessingDocument(
        qProcessingUser.processingDocument.getMetadata().getName());
    QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;
    QIncomingDocument incomingDocument = new QIncomingDocument(
        qProcessingDocument.incomingDoc.getMetadata().getName());
    return Optional.ofNullable(selectFrom(qProcessingUser)
        .select(qProcessingUser.processingDuration)
        .innerJoin(qProcessingDocument)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id)
            .and(qProcessingUser.user.id.eq(userId)))
        .innerJoin(qProcessingUserRole)
        .on(qProcessingUserRole.processingUser.id.eq(qProcessingUser.id)
            .and(qProcessingUserRole.role.eq(ASSIGNEE)))
        .innerJoin(qProcessingDocument.incomingDoc, incomingDocument)
        .on(incomingDocument.id.eq(incomingDocumentId))
        .fetchFirst());
  }
}

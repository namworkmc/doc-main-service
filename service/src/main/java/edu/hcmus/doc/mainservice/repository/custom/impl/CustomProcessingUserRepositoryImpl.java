package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.ASSIGNEE;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.COLLABORATOR;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.REPORTER;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CustomProcessingUserRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingUser>
    implements CustomProcessingUserRepository {

  private static final QProcessingUser qProcessingUser = QProcessingUser.processingUser;
  private static final QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;

  private static final QProcessingDocument qProcessingDocument = QProcessingDocument.processingDocument;

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
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.processingDocument.incomingDoc.id.eq(incomingDocumentId)
            .and(processingUser.user.id.eq(SecurityUtils.getCurrentUser().getId()))
            .and(processingUser.step.eq(step))
            .and(qProcessingUserRole.role.eq(ASSIGNEE)))
        .fetchFirst() != null;
  }

  /**
   * This function is used to get date expired of a user in a processing document, but it cant
   * detect if the processing duration is infinite or not
   *
   * @param incomingDocumentId Long
   * @param userId             Long
   * @param userRole           DocSystemRoleEnum
   * @param isAnyRole          Boolean
   * @return Optional<LocalDate>
   */
  @Override
  public Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId,
      DocSystemRoleEnum userRole
      , Boolean isAnyRole) {
    QProcessingDocument qProcessingDocument = new QProcessingDocument(
        qProcessingUser.processingDocument.getMetadata().getName());
    QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;
    QIncomingDocument incomingDocument = new QIncomingDocument(
        qProcessingDocument.incomingDoc.getMetadata().getName());

    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (Boolean.TRUE.equals(isAnyRole)) {
      if (userRole.equals(DocSystemRoleEnum.VAN_THU)) {
        // If isAnyRole is true and userRole is VANTHU, select roles: ASSIGNEE, COLLABORATOR, and REPORTER
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR, REPORTER));
      } else {
        // If isAnyRole is true and userRole is not VANTHU, select roles: ASSIGNEE, COLLABORATOR
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR));
      }
    } else {
      // If isAnyRole is false, select role: ASSIGNEE
      whereBuilder.and(qProcessingUserRole.role.eq(ASSIGNEE));
    }

    return Optional.ofNullable(selectFrom(qProcessingUser)
        .select(qProcessingUser.processingDuration)
        .innerJoin(qProcessingDocument)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id)
            .and(qProcessingUser.user.id.eq(userId)))
        .innerJoin(qProcessingUserRole)
        .on(qProcessingUserRole.processingUser.id.eq(qProcessingUser.id)
            .and(whereBuilder))
        .innerJoin(qProcessingDocument.incomingDoc, incomingDocument)
        .on(incomingDocument.id.eq(incomingDocumentId))
        .fetchFirst());
  }

  /**
   * This function is used to get date expired of a user in a processing document, but it cant
   * detect if the processing duration is infinite or not
   *
   * @param documentId
   * @param userId
   * @param userRole
   * @param isAnyRole
   * @return
   */
  @Override
  public Optional<String> getDateExpiredV2(Long documentId, Long userId,
      DocSystemRoleEnum userRole, Boolean isAnyRole, ProcessingDocumentTypeEnum type) {
    QProcessingDocument qProcessingDocument = new QProcessingDocument(
        qProcessingUser.processingDocument.getMetadata().getName());
    QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;
    QIncomingDocument incomingDocument = new QIncomingDocument(
        qProcessingDocument.incomingDoc.getMetadata().getName());
    QOutgoingDocument outgoingDocument = new QOutgoingDocument(
        qProcessingDocument.outgoingDocument.getMetadata().getName());

    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (Boolean.TRUE.equals(isAnyRole)) {
      if ((type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT && userRole.equals(
          DocSystemRoleEnum.VAN_THU)) || (
          type == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT && userRole.equals(
              DocSystemRoleEnum.CHUYEN_VIEN))) {
        // If isAnyRole is true and userRole is VANTHU, select roles: ASSIGNEE, COLLABORATOR, and REPORTER
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR, REPORTER));
      } else {
        // If isAnyRole is true and userRole is not VANTHU, select roles: ASSIGNEE, COLLABORATOR
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR));
      }
    } else {
      // If isAnyRole is false, select role: ASSIGNEE
      whereBuilder.and(qProcessingUserRole.role.eq(ASSIGNEE));
    }

    JPAQuery<Tuple> jpaQuery = selectFrom(qProcessingUser)
        .select(qProcessingUser.processingDuration,
            qProcessingUser.id)
        .innerJoin(qProcessingDocument)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id)
            .and(qProcessingUser.user.id.eq(userId)))
        .innerJoin(qProcessingUserRole)
        .on(qProcessingUserRole.processingUser.id.eq(qProcessingUser.id)
            .and(whereBuilder));

    // if type is INCOMING then join with incomingDocument, else join with outgoingDocument
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      jpaQuery.innerJoin(qProcessingDocument.incomingDoc, incomingDocument)
          .on(incomingDocument.id.eq(documentId));
    } else {
      jpaQuery.innerJoin(qProcessingDocument.outgoingDocument, outgoingDocument)
          .on(outgoingDocument.id.eq(documentId));
    }

    Tuple tuple = jpaQuery.fetchFirst();

    // if tuple is null, return null
    if (tuple == null) {
      return Optional.empty();
    }
    // if id is not null, and processingDuration is null, return infinite
    if (tuple.get(qProcessingUser.id) != null
        && tuple.get(qProcessingUser.processingDuration) == null) {
      return Optional.of("infinite");
    }
    // if id is not null, and processingDuration is not null, return processingDuration
    if (tuple.get(qProcessingUser.id) != null
        && tuple.get(qProcessingUser.processingDuration) != null) {
      return Optional.of(tuple.get(qProcessingUser.processingDuration).toString());
    }
    // if not match any case, return null
    return Optional.empty();
  }

  @Override
  public List<ProcessingUser> findByUserIdAndProcessingDocumentIdWithRole(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId)))
        .orderBy(qProcessingUserRole.role.asc())
        .fetch();
  }

  @Override
  public ProcessingUser findByUserIdAndProcessingDocumentIdAndRoleAssignee(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId))
            .and(qProcessingUserRole.role.eq(ASSIGNEE)))
        .fetchFirst();
  }

  @Override
  public List<ProcessingUser> findByDocumentIdAndStep(Long documentId, Integer step,
      ProcessingDocumentTypeEnum type) {
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(processingUser.step.eq(step));
    if (type.equals(ProcessingDocumentTypeEnum.INCOMING_DOCUMENT)) {
      whereBuilder.and(qProcessingDocument.incomingDoc.id.eq(documentId));
    } else {
      whereBuilder.and(qProcessingDocument.outgoingDocument.id.eq(documentId));
    }

    return selectFrom(processingUser)
        .leftJoin(qProcessingDocument)
        .on(qProcessingUser.processingDocument.id.eq(qProcessingDocument.id))
        .where(whereBuilder)
        .fetch();
  }
}

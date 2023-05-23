package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.model.entity.QExtendRequest;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomExtendRequestRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomExtendRequestRepositoryImpl
    extends DocAbstractCustomRepository<ExtendRequest>
    implements CustomExtendRequestRepository {

  private static final QExtendRequest qExtendRequest = QExtendRequest.extendRequest;

  @Override
  public List<ExtendRequest> getExtendRequestsByUsername(String username) {
    return selectFrom(qExtendRequest)
        .innerJoin(qExtendRequest.processingUser, processingUser)
        .fetchJoin()
        .innerJoin(processingUser.processingDocument, processingDocument)
        .fetchJoin()
        .innerJoin(processingDocument.incomingDoc, incomingDocument)
        .fetchJoin()
        .where(qExtendRequest.createdBy.eq(username))
        .orderBy(qExtendRequest.createdDate.desc())
        .fetch();
  }

  @Override
  public boolean canValidateExtendRequest(Long extendRequestId, Long validatorId) {
    QProcessingUser qProcessingUser = new QProcessingUser(qExtendRequest.processingUser.getMetadata().getName());
    QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;
    QUser qUser = new QUser(qProcessingUser.user.getMetadata().getName());

    JPAQuery<Long> subQuery = selectFrom(qExtendRequest)
        .select(qProcessingUser.id)
        .innerJoin(qExtendRequest.processingUser, qProcessingUser)
        .where(qExtendRequest.id.eq(extendRequestId));

    return selectFrom(qExtendRequest)
        .innerJoin(qExtendRequest.processingUser, processingUser)
        .innerJoin(qProcessingUserRole)
        .on(qProcessingUserRole.processingUser.id.eq(processingUser.id))
        .innerJoin(qUser).on(qUser.id.eq(qProcessingUser.user.id))
        .where(qProcessingUser.id.in(subQuery)
            .and(qProcessingUser.user.id.eq(validatorId))
            .and(qProcessingUserRole.role.eq(ProcessingDocumentRoleEnum.REPORTER)))
        .fetchFirst() != null;
  }
}

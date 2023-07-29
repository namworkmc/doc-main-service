package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.entity.QComment;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomCommentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Optional;

public class CustomCommentRepositoryImpl
    extends DocAbstractCustomRepository<Comment>
    implements CustomCommentRepository {

  private static final QComment qComment = QComment.comment;

  @Override
  public long getTotalElementsByTypeAndDocumentId(ProcessingDocumentTypeEnum type, Long incomingDocumentId) {
    JPAQuery<Long> query = selectFrom(qComment)
        .select(qComment.id.count())
        .innerJoin(qComment.incomingDocument, incomingDocument)
        .where(incomingDocument.id.eq(incomingDocumentId));

    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      query.innerJoin(qComment.incomingDocument, incomingDocument)
          .where(incomingDocument.id.eq(incomingDocumentId));
    } else if (type == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      query.innerJoin(qComment.outgoingDocument, outgoingDocument)
          .where(outgoingDocument.id.eq(incomingDocumentId));
    }

    return Optional.ofNullable(query.fetchFirst()).orElse(0L);
  }

  @Override
  public List<Comment> getCommentByIncomingDocumentId(long page, long pageSize, Long incomingDocumentId) {
    return buildPaginationQuery(page, pageSize)
        .innerJoin(qComment.incomingDocument, incomingDocument)
        .where(incomingDocument.id.eq(incomingDocumentId))
        .orderBy(qComment.createdDate.desc())
        .fetch();
  }

  @Override
  public List<Comment> getCommentByOutgoingDocumentId(long page, long pageSize, Long documentId) {
    return buildPaginationQuery(page, pageSize)
        .innerJoin(qComment.outgoingDocument, outgoingDocument)
        .where(outgoingDocument.id.eq(documentId))
        .orderBy(qComment.createdDate.desc())
        .fetch();
  }

  @Override
  public JPAQuery<Comment> buildPaginationQuery(Long page, Long pageSize) {
    return selectFrom(qComment)
        .offset(page * pageSize)
        .limit(pageSize);
  }
}

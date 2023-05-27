package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QComment.comment;
import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.repository.custom.CustomCommentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomCommentRepositoryImpl
    extends DocAbstractCustomRepository<Comment>
    implements CustomCommentRepository {

  @Override
  public List<Comment> getCommentByIncomingDocumentId(Long incomingDocumentId) {
    return selectFrom(comment)
        .innerJoin(comment.incomingDocument, incomingDocument)
        .where(incomingDocument.id.eq(incomingDocumentId))
        .orderBy(comment.createdDate.desc())
        .fetch();
  }

  @Override
  public List<Comment> getCommentByOutgoingDocumentId(Long outgoingDocumentId) {

    return selectFrom(comment)
        .innerJoin(comment.outgoingDocument, outgoingDocument)
        .where(outgoingDocument.id.eq(outgoingDocumentId))
        .orderBy(comment.createdDate.desc())
        .fetch();
  }
}

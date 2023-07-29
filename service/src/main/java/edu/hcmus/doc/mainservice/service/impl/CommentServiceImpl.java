package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.CommentRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final IncomingDocumentRepository incomingDocumentRepository;
  private final OutgoingDocumentRepository outgoingDocumentRepository;

  @Override
  public long getTotalElementsByDocumentId(ProcessingDocumentTypeEnum processingDocumentType, Long documentId) {
    return commentRepository.getTotalElementsByTypeAndDocumentId(processingDocumentType, documentId);
  }

  @Override
  public List<Comment> getCommentByTypeAndDocumentId(long page, long pageSize,
      ProcessingDocumentTypeEnum processingDocumentType, Long documentId) {
    if (processingDocumentType == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      return commentRepository.getCommentByIncomingDocumentId(page, pageSize, documentId);
    } else {
      return commentRepository.getCommentByOutgoingDocumentId(page, pageSize, documentId);
    }
  }

  @Override
  public Long createComment(
      Long documentId,
      ProcessingDocumentTypeEnum processingDocumentType,
      Comment comment) {

    if (processingDocumentType == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      comment.setOutgoingDocument(
          outgoingDocumentRepository
              .findById(documentId)
              .orElseThrow(() -> new DocumentNotFoundException(
                  DocumentNotFoundException.OUTGOING_DOCUMENT_NOT_FOUND)));
    } else {
      IncomingDocument incomingDocument =
          incomingDocumentRepository
              .findById(documentId)
              .orElseThrow(() -> new DocumentNotFoundException(
                  DocumentNotFoundException.OUTGOING_DOCUMENT_NOT_FOUND));
      comment.setIncomingDocument(incomingDocument);
    }
    return commentRepository.save(comment).getId();
  }
}

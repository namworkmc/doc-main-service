package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.CommentRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
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

  @Override
  public List<Comment> getCommentByIncomingDocumentId(Long incomingDocumentId) {
    return commentRepository.getCommentByIncomingDocumentId(incomingDocumentId);
  }

  @Override
  public Long createComment(Long incomingDocumentId, Comment comment) {
    IncomingDocument incomingDocument = incomingDocumentRepository.findById(incomingDocumentId)
        .orElseThrow(() -> new IncomingDocumentNotFoundException(INCOMING_DOCUMENT_NOT_FOUND));
    comment.setIncomingDocument(incomingDocument);
    return commentRepository.save(comment).getId();
  }
}

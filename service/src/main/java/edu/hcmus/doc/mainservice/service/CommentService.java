package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import java.util.List;

public interface CommentService {

  List<Comment> getCommentByIncomingDocumentId(Long incomingDocumentId);

  Long createComment(Long incomingDocumentId, Comment comment);
}

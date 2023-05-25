package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import java.util.List;

public interface CustomCommentRepository {

  List<Comment> getCommentByIncomingDocumentId(Long incomingDocumentId);

  List<Comment> getCommentByOutgoingDocumentId(Long outgoingDocumentId);
}

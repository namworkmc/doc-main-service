package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface CommentService {

  List<Comment> getCommentByTypeAndDocumentId(ProcessingDocumentTypeEnum processingDocumentType,
      Long incomingDocumentId);

  Long createComment(Long documentId, ProcessingDocumentTypeEnum processingDocumentType,
      Comment comment);
}

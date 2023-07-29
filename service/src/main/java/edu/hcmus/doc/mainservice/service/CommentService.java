package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface CommentService {

  long getTotalElementsByDocumentId(ProcessingDocumentTypeEnum processingDocumentType, Long documentId);

  List<Comment> getCommentByTypeAndDocumentId(long page, long pageSize, ProcessingDocumentTypeEnum processingDocumentType,
      Long documentId);

  Long createComment(Long documentId, ProcessingDocumentTypeEnum processingDocumentType,
      Comment comment);
}

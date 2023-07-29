package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.DocAbstractPagination;
import java.util.List;

public interface CustomCommentRepository
    extends DocAbstractPagination<Comment, Long, Long> {

  long getTotalElementsByTypeAndDocumentId(ProcessingDocumentTypeEnum type, Long incomingDocumentId);

  List<Comment> getCommentByIncomingDocumentId(long page, long pageSize, Long incomingDocumentId);

  List<Comment> getCommentByOutgoingDocumentId(long page, long pageSize, Long documentId);
}

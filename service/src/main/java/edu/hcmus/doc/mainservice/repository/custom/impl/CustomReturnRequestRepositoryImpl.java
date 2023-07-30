package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;

import com.querydsl.core.BooleanBuilder;
import edu.hcmus.doc.mainservice.model.entity.QReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Optional;

public class CustomReturnRequestRepositoryImpl extends DocAbstractCustomRepository<ReturnRequest>
    implements CustomReturnRequestRepository {

  private static final QReturnRequest qReturnRequest = QReturnRequest.returnRequest;

  @Override
  public List<ReturnRequest> getReturnRequestsByDocumentId(Long documentId,
      ProcessingDocumentTypeEnum type) {
    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      whereBuilder.and(incomingDocument.id.eq(documentId));
    } else {
      whereBuilder.and(outgoingDocument.id.eq(documentId));
    }

    return selectFrom(qReturnRequest)
        .where(whereBuilder)
        .fetch();
  }

  @Override
  public Optional<ReturnRequest> getReturnRequestById(Long returnRequestId) {
    return Optional.ofNullable(selectFrom(qReturnRequest)
        .where(qReturnRequest.id.eq(returnRequestId))
        .fetchFirst());
  }
}

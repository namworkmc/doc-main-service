package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;
import java.util.Optional;

public interface CustomReturnRequestRepository {

  List<ReturnRequest> getReturnRequestsByDocumentId(Long documentId, ProcessingDocumentTypeEnum type);
  Optional<ReturnRequest> getReturnRequestById(Long returnRequestId);
}

package edu.hcmus.doc.mainservice.repository.custom;


import edu.hcmus.doc.mainservice.model.dto.TransferHistory.GetTransferDocumentHistoryResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;

public interface CustomTransferHistoryRepository extends
    DocAbstractSearchRepository<TransferHistory, TransferHistorySearchCriteriaDto> {
  GetTransferDocumentHistoryResponse getTransferDocumentHistory(Long currentUserId);
}

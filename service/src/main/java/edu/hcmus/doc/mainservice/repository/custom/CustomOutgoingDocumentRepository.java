package edu.hcmus.doc.mainservice.repository.custom;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.util.List;

public interface CustomOutgoingDocumentRepository
        extends DocAbstractSearchRepository<OutgoingDocument, OutgoingDocSearchCriteriaDto> {
  OutgoingDocument getOutgoingDocumentById(Long id);

  List<OutgoingDocument> getOutgoingDocumentsByIds(List<Long> ids);

  List<OutgoingDocument> getDocumentsLinkedToIncomingDocument(Long sourceDocumentId);

  List<Long> getOutgoingDocumentsWithTransferPermission();
}
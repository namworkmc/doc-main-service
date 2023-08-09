package edu.hcmus.doc.mainservice.repository.custom;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.util.List;
import java.util.Map;

public interface CustomOutgoingDocumentRepository
        extends DocAbstractSearchRepository<OutgoingDocument, OutgoingDocSearchCriteriaDto> {
  OutgoingDocument getOutgoingDocumentById(Long id);

  List<OutgoingDocument> getOutgoingDocumentsByIds(List<Long> ids);

  List<OutgoingDocument> getDocumentsLinkedToIncomingDocument(Long sourceDocumentId);

  List<Long> getOutgoingDocumentsWithTransferPermission();

  boolean isDocumentReleased(Long documentId);

  List<Long>  checkOutgoingDocumentSearchByCriteria(long userId, int step, ProcessingDocumentRoleEnum role);

  Map<Long, String> getProcessingTimeOfOutgoingDocumentList(long userId);

  List<OutgoingDocument> searchALlByCriteria(OutgoingDocSearchCriteriaDto searchCriteriaDto);

}
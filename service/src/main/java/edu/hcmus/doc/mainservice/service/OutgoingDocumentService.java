package edu.hcmus.doc.mainservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import java.util.List;

public interface OutgoingDocumentService {
  OutgoingDocument createOutgoingDocument(OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto)
          throws JsonProcessingException;

  long getTotalElements(OutgoingDocSearchCriteriaDto searchCriteriaDto);

  long getTotalPages(OutgoingDocSearchCriteriaDto searchCriteriaDto, long limit);

  List<OutgoingDocument> searchOutgoingDocuments(OutgoingDocSearchCriteriaDto searchCriteria, int page, int pageSize);

}

package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;

public interface CustomOutgoingDocumentRepository
    extends DocAbstractSearchRepository<OutgoingDocument, OutgoingDocSearchCriteriaDto> {
}

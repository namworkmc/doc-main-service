package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.DocumentTypeSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;

public interface CustomDocumentTypeRepository
    extends DocAbstractSearchRepository<DocumentType, DocumentTypeSearchCriteria> {

}

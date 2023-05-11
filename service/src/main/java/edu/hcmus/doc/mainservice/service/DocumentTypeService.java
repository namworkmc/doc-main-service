package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import java.util.List;

public interface DocumentTypeService extends SearchService<DocumentTypeDto, DocumentTypeSearchCriteria> {

  List<DocumentType> findAll();

  DocumentType findById(Long id);

  DocumentType saveDocumentType(DocumentType documentType);

  void deleteDocumentTypes(List<Long> documentTypeIds);
}

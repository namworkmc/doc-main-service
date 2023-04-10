package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import java.util.List;

public interface DocumentTypeService {

  List<DocumentType> findAll();

  DocumentType findById(Long id);

  DocumentType createDocumentType(DocumentType documentType);
}

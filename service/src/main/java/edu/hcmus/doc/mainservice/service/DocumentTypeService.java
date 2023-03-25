package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;

import java.util.List;

public interface DocumentTypeService {
    public List<DocumentType> findAll();
    public DocumentType findById(Long id);
}

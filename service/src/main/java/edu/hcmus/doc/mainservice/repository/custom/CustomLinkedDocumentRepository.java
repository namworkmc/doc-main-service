package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.LinkedDocument;

public interface CustomLinkedDocumentRepository {
  LinkedDocument getLinkedDocument(Long incomingDocumentId, Long outgoingDocumentId);
}
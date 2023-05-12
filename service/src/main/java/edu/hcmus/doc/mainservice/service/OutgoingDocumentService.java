package edu.hcmus.doc.mainservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;

public interface OutgoingDocumentService {
  OutgoingDocument createOutgoingDocument(OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto)
          throws JsonProcessingException;
}

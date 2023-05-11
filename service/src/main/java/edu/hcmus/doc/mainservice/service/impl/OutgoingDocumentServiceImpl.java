package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class OutgoingDocumentServiceImpl implements OutgoingDocumentService {
  private final OutgoingDocumentRepository outgoingDocumentRepository;

  @Override
  public OutgoingDocument createOutgoingDocument(OutgoingDocument outgoingDocument) {
    return outgoingDocumentRepository.save(outgoingDocument);
  }
}

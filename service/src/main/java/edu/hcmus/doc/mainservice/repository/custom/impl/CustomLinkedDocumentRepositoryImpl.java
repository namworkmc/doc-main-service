package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.LinkedDocument;
import edu.hcmus.doc.mainservice.repository.custom.CustomLinkedDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;

import static edu.hcmus.doc.mainservice.model.entity.QLinkedDocument.linkedDocument;

public class CustomLinkedDocumentRepositoryImpl
    extends DocAbstractCustomRepository<LinkedDocument>
    implements CustomLinkedDocumentRepository {

    @Override
    public LinkedDocument getLinkedDocument(Long incomingDocumentId, Long outgoingDocumentId) {
        return selectFrom(linkedDocument)
                .where(linkedDocument.incomingDocument.id.eq(incomingDocumentId)
                .and(linkedDocument.outgoingDocument.id.eq(outgoingDocumentId)))
                .fetchFirst();
    }
}

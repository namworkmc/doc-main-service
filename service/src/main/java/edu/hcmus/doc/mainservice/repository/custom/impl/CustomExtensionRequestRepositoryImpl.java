package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QExtensionRequest.extensionRequest;
import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import edu.hcmus.doc.mainservice.repository.custom.CustomExtensionRequestRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomExtensionRequestRepositoryImpl
    extends DocAbstractCustomRepository<ExtensionRequest>
    implements CustomExtensionRequestRepository {

  @Override
  public List<ExtensionRequest> getExtensionRequestsByUsername(String username) {
    return selectFrom(extensionRequest)
        .innerJoin(extensionRequest.processingUser, processingUser)
        .fetchJoin()
        .innerJoin(processingUser.processingDocument, processingDocument)
        .fetchJoin()
        .innerJoin(processingDocument.incomingDoc, incomingDocument)
        .fetchJoin()
        .where(extensionRequest.createdBy.eq(username))
        .orderBy(extensionRequest.createdDate.desc())
        .fetch();
  }
}

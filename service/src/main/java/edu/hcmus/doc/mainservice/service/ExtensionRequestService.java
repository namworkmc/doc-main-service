package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import java.util.List;

public interface ExtensionRequestService {

  List<ExtensionRequest> getExtensionRequestsByUsername(String username);

  Long createExtensionRequest(Long processingDocId, ExtensionRequest extensionRequest);

  Long validateExtensionRequest(Long id, Long validatorId, ExtensionRequestStatus status);
}

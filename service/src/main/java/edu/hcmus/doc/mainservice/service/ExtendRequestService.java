package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import java.util.List;

public interface ExtendRequestService {

  List<ExtendRequest> getExtensionRequestsByUsername(String username);

  Long createExtensionRequest(Long processingDocId, ExtendRequest extendRequest);

  Long validateExtensionRequest(Long id, ExtendRequestStatus status);

  boolean canCurrentUserValidateExtendRequest(Long extendRequestId);
}

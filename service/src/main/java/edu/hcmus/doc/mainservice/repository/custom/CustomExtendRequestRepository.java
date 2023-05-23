package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import java.util.List;

public interface CustomExtendRequestRepository {

  List<ExtendRequest> getExtendRequestsByUsername(String username);

  boolean canValidateExtendRequest(Long extendRequestId, Long validatorId);
}

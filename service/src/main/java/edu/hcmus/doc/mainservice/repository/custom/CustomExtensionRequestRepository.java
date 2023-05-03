package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import java.util.List;

public interface CustomExtensionRequestRepository {

  List<ExtensionRequest> getExtensionRequestsByUsername(String username);
}

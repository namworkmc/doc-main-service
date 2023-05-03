package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException.EXTENSION_REQUEST_NOT_FOUND;
import static edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.ExtensionRequestRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.ExtensionRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ExtensionRequestServiceImpl implements ExtensionRequestService {

  private final ExtensionRequestRepository extensionRequestRepository;

  private final ProcessingUserRepository processingUserRepository;
  private final UserRepository userRepository;

  @Override
  public List<ExtensionRequest> getExtensionRequestsByUsername(String username) {
    return extensionRequestRepository.getExtensionRequestsByUsername(username);
  }

  @Override
  public Long createExtensionRequest(Long processingDocId, ExtensionRequest extensionRequest) {
    processingUserRepository.findById(processingDocId)
        .ifPresentOrElse(processingUser -> {
              extensionRequest.setProcessingUser(processingUser);
              extensionRequest.setStatus(ExtensionRequestStatus.PENDING);
            },
            () -> {
              throw new ProcessingDocumentNotFoundException(PROCESSING_DOCUMENT_NOT_FOUND);
            });
    return extensionRequestRepository.save(extensionRequest).getId();
  }

  @Override
  public Long validateExtensionRequest(Long id, Long validatorId, ExtensionRequestStatus status) {
    ExtensionRequest extensionRequest = extensionRequestRepository
        .findById(id)
        .orElseThrow(() -> new ExtensionRequestNotFoundException(EXTENSION_REQUEST_NOT_FOUND));

    if (status == ExtensionRequestStatus.APPROVED) {
      extensionRequest.getProcessingUser()
          .setProcessingDuration(extensionRequest.getExtendedUntil());
    } else {
      extensionRequest.getProcessingUser()
          .setProcessingDuration(extensionRequest.getOldExpiredDate());
    }

    extensionRequest.setValidatedBy(userRepository
        .findById(validatorId)
        .orElseThrow(UserNotFoundException::new));
    extensionRequest.setStatus(status);
    return extensionRequestRepository.save(extensionRequest).getId();
  }
}

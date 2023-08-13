package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.formatDocIds;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestPostDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ReturnRequestType;
import edu.hcmus.doc.mainservice.model.exception.DocumentAlreadyClosedOrReleasedException;
import edu.hcmus.doc.mainservice.model.exception.DocumentAlreadyProcessedByNextUserInFlow;
import edu.hcmus.doc.mainservice.model.exception.DocumentAlreadyProcessedByYou;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ReturnRequestNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotAllowedToCreateSendBackRequestException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.TransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.EmailService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.service.ReturnRequestService;
import edu.hcmus.doc.mainservice.util.TransferDocumentUtils;
import edu.hcmus.doc.mainservice.util.mapper.ReturnRequestMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ReturnRequestServiceImpl implements ReturnRequestService {

  private final ReturnRequestRepository returnRequestRepository;

  private final UserRepository userRepository;

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final OutgoingDocumentRepository outgoingDocumentRepository;

  private final ReturnRequestMapper returnRequestMapper;

  private final ProcessingUserRepository processingUserRepository;

  private final ProcessingUserRoleRepository processingUserRoleRepository;

  private final TransferHistoryRepository transferHistoryRepository;

  private final DocumentReminderRepository documentReminderRepository;

  private final ProcessingDocumentRepository processingDocumentRepository;

  private final ProcessingDocumentService processingDocumentService;

  private final EmailService emailService;

  @Override
  public List<ReturnRequestGetDto> getReturnRequestsByDocumentId(Long documentId,
      ProcessingDocumentTypeEnum type) {
    return returnRequestRepository.getReturnRequestsByDocumentId(documentId, type).stream()
        .map(returnRequest -> returnRequestMapper.toReturnRequestGetDto(returnRequest, type))
        .toList();
  }

  @Override
  public ReturnRequestGetDto getReturnRequestById(Long returnRequestId,
      ProcessingDocumentTypeEnum type) {
    ReturnRequest returnRequest = returnRequestRepository.getReturnRequestById(returnRequestId)
        .orElseThrow(() -> new ReturnRequestNotFoundException(
            ReturnRequestNotFoundException.RETURN_REQUEST_NOT_FOUND));
    return returnRequestMapper.toReturnRequestGetDto(returnRequest, type);
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public List<Long> createSendBackRequest(ReturnRequestPostDto returnRequestDto) {
    User currentUser = SecurityUtils.getCurrentUser();
    List<ReturnRequest> returnRequests = new ArrayList<>();
    ProcessingDocumentTypeEnum type = returnRequestDto.getDocumentType();
    ReturnRequestType returnRequestType = returnRequestDto.getReturnRequestType();
    returnRequestDto.getDocumentIds().forEach(documentId -> {
      ReturnRequest returnRequest = new ReturnRequest();
      Boolean isDocTransferredByCurrentUser = false;
      boolean isDocumentClosedOrReleased = false;
      // validate users
      validateUsers(returnRequestDto, returnRequest);
      // validate document
      if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
        // neu la van thu thi khong duoc tra lai
        if(currentUser.getRole().equals(DocSystemRoleEnum.VAN_THU)) {
          throw new UserNotAllowedToCreateSendBackRequestException();
        }
        IncomingDocument incomingDocument = incomingDocumentRepository
            .findById(documentId)
            .orElseThrow(
                () -> new DocumentNotFoundException(DocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND));
        returnRequest.setIncomingDocument(incomingDocument);

        // case SEND_BACK: check if you have transferred the document to the next user or not
        int step = TransferDocumentUtils.getStep(currentUser, null, true);
        if (returnRequestType.equals(ReturnRequestType.SEND_BACK)) {
          isDocTransferredByCurrentUser = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
              GetTransferDocumentDetailRequest.builder()
                  .documentId(incomingDocument.getId())
                  .userId(currentUser.getId())
                  .role(ProcessingDocumentRoleEnum.REPORTER)
                  .step(step)
                  .build());
          isDocumentClosedOrReleased = processingDocumentRepository.isDocumentClosed(documentId, type);
        }
      } else {
        OutgoingDocument outgoingDocument = outgoingDocumentRepository.findById(documentId)
            .orElseThrow(
                () -> new DocumentNotFoundException(DocumentNotFoundException.OUTGOING_DOCUMENT_NOT_FOUND));
        returnRequest.setOutgoingDocument(outgoingDocument);
        // neu la nguoi tao ra van ban => nguoi dau tien trong luong => khong duoc tra lai
        if (outgoingDocument.getCreatedBy().equals(currentUser.getUsername())) {
          throw new UserNotAllowedToCreateSendBackRequestException();
        }

        int step = TransferDocumentUtils.getStepOutgoingDocument(currentUser, true);
        if (returnRequestType.equals(ReturnRequestType.SEND_BACK)) {
          // case SEND_BACK: check if you have transferred the document to the next user or not
          isDocTransferredByCurrentUser = processingDocumentService.isUserWorkingOnOutgoingDocumentWithSpecificRole(
              GetTransferDocumentDetailRequest.builder()
                  .documentId(outgoingDocument.getId())
                  .userId(currentUser.getId())
                  .role(ProcessingDocumentRoleEnum.REPORTER)
                  .step(step)
                  .build());
          isDocumentClosedOrReleased = outgoingDocumentRepository.isDocumentReleased(documentId);
        }
      }
      // if the document is processed by current user or document is released or closed
      // throw exception because you can not send back the document after transferred
      if (isDocTransferredByCurrentUser) {
        throw new DocumentAlreadyProcessedByYou();
      }
      if (isDocumentClosedOrReleased) {
        throw new DocumentAlreadyClosedOrReleasedException();
      }

      returnRequest.setReason(returnRequestDto.getReason());
      returnRequest.setStatus(ExtendRequestStatus.APPROVED);
      returnRequest.setType(returnRequestType);
      returnRequests.add(returnRequest);
    });

    // delete records in processing user and processing user role
    returnRequestDto.getDocumentIds().forEach(documentId -> {
      // step o day se bang step cua previous user
      List<ProcessingUser> processingUsers = processingUserRepository.findByDocumentIdAndStep(
          documentId, returnRequestDto.getStep(), type);
      if (processingUsers.isEmpty()) {
        throw new ProcessingDocumentNotFoundException(
            ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND);
      }
      ProcessingDocument processingDocument = processingDocumentRepository.findProcessingDocumentByProcessingUserId(processingUsers.get(0).getId()).orElseThrow(
          () -> new ProcessingDocumentNotFoundException(
              ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND));
//      ProcessingDocument processingDocument = processingUsers.get(0).getProcessingDocument();

      processingUsers.forEach(processingUser -> {
        ProcessingUserRole processingUserRole = processingUserRoleRepository.findByProcessingUserId(
            processingUser.getId());

        // reporter chinh la nguoi truoc do
        if (processingUserRole.getRole().equals(ProcessingDocumentRoleEnum.REPORTER)) {
          returnRequests.forEach(returnRequest -> {
            returnRequest.setPreviousProcessingUser(processingUser.getUser());
          });
        }
        processingUserRoleRepository.delete(processingUserRole);
        documentReminderRepository.deleteByProcessingUserId(processingUser.getId());
        processingUserRepository.delete(processingUser);
      });

      // if no user working on document, delete processing document record
      // this is to handle case VAN_THU or CHUYEN_VIEN create return request
      if (Boolean.FALSE.equals(
          processingDocumentRepository.isExistUserWorkingOnThisDocumentAtSpecificStep(
              processingDocument.getId(), null))) {
        processingDocumentRepository.delete(processingDocument);
        if (type.equals(ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT)) {
          // neu la outgoing document thi update status outgoing document ve unprocessed
          returnRequestDto.getDocumentIds().forEach(outgoingDocumentId -> {
            outgoingDocumentRepository.updateStatusById(outgoingDocumentId, OutgoingDocumentStatusEnum.UNPROCESSED);
          });
        }
      }
    });
    // save to DB
    returnRequestRepository.saveAll(returnRequests);
    // create new history transfer record
    returnRequests.forEach(returnRequest -> {
      TransferHistory transferHistory = createTransferHistory(returnRequest, type);
      transferHistoryRepository.save(transferHistory);
    });
    // send email
    // send back: current = sender, previous = receiver
    User currentProcessingUser = returnRequests.get(0).getCurrentProcessingUser();
    User previousProcessingUser = returnRequests.get(0).getPreviousProcessingUser();
    emailService.sendSendBackDocumentEmail(currentProcessingUser.getFullName(), previousProcessingUser.getEmail(),
        previousProcessingUser.getFullName(), formatDocIds(returnRequestDto.getDocumentIds()));
    return returnRequests.stream().map(ReturnRequest::getId).toList();
  }

  @Override
  public List<Long> createReturnRequest(ReturnRequestPostDto returnRequestDto) {
    User currentUser = SecurityUtils.getCurrentUser();
    List<ReturnRequest> returnRequests = new ArrayList<>();
    ProcessingDocumentTypeEnum type = returnRequestDto.getDocumentType();
    ReturnRequestType returnRequestType = returnRequestDto.getReturnRequestType();
    returnRequestDto.getDocumentIds().forEach(documentId -> {
      ReturnRequest returnRequest = new ReturnRequest();
      Boolean isDocumentProcessedByNextUserInFlow = false;
      // validate users
      validateUsers(returnRequestDto, returnRequest);
      // validate document
      if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
        IncomingDocument incomingDocument = incomingDocumentRepository
            .findById(documentId)
            .orElseThrow(
                () -> new DocumentNotFoundException(DocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND));
        returnRequest.setIncomingDocument(incomingDocument);

        // case WITHDRAW: check if the document is already processed by higher role or not
        int step = TransferDocumentUtils.getStep(currentUser, null, true);
        if (returnRequestType.equals(ReturnRequestType.WITHDRAW)) {
          isDocumentProcessedByNextUserInFlow = processingDocumentService.isExistUserWorkingOnThisDocumentAtSpecificStep(
              incomingDocument.getId(), step + 1, type);
        }
      } else {
        OutgoingDocument outgoingDocument = outgoingDocumentRepository.findById(documentId)
            .orElseThrow(
                () -> new DocumentNotFoundException(DocumentNotFoundException.OUTGOING_DOCUMENT_NOT_FOUND));
        returnRequest.setOutgoingDocument(outgoingDocument);

        // check if the document is already processed by higher role or not
        int step = TransferDocumentUtils.getStepOutgoingDocument(currentUser, true);
        if (returnRequestType.equals(ReturnRequestType.WITHDRAW)) {
          isDocumentProcessedByNextUserInFlow = processingDocumentService.isExistUserWorkingOnThisDocumentAtSpecificStep(
              outgoingDocument.getId(), step + 1, type);
        }
      }
      // check if the document is already processed by higher role or not, if yes, throw exception
      if (returnRequestType.equals(ReturnRequestType.WITHDRAW)
          && isDocumentProcessedByNextUserInFlow) {
        throw new DocumentAlreadyProcessedByNextUserInFlow();
      }

      returnRequest.setReason(returnRequestDto.getReason());
      returnRequest.setStatus(ExtendRequestStatus.APPROVED);
      returnRequest.setType(returnRequestType);
      returnRequests.add(returnRequest);
    });
    // save to DB
    returnRequestRepository.saveAll(returnRequests);

    // create new history transfer record
    returnRequests.forEach(returnRequest -> {
      TransferHistory transferHistory = createTransferHistory(returnRequest, type);
      transferHistoryRepository.save(transferHistory);
    });

    // delete records in processing user and processing user role
    returnRequestDto.getDocumentIds().forEach(documentId -> {
      List<ProcessingUser> processingUsers = processingUserRepository.findByDocumentIdAndStep(
          documentId, returnRequestDto.getStep(), type);

      if (processingUsers.isEmpty()) {
        throw new ProcessingDocumentNotFoundException(
            ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND);
      }
      ProcessingDocument processingDocument = processingUsers.get(0).getProcessingDocument();

      processingUsers.forEach(processingUser -> {
        ProcessingUserRole processingUserRole = processingUserRoleRepository.findByProcessingUserId(
            processingUser.getId());

        processingUserRoleRepository.delete(processingUserRole);
        documentReminderRepository.deleteByProcessingUserId(processingUser.getId());
        processingUserRepository.delete(processingUser);
      });

      // if no user working on document, delete processing document record
      // this is to handle case VAN_THU or CHUYEN_VIEN create return request
      if (Boolean.FALSE.equals(
          processingDocumentRepository.isExistUserWorkingOnThisDocumentAtSpecificStep(
              processingDocument.getId(), null))) {
        processingDocumentRepository.delete(processingDocument);
        if (type.equals(ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT)) {
          // neu la outgoing document thi update status outgoing document ve unprocessed
          returnRequestDto.getDocumentIds().forEach(outgoingDocumentId -> {
            outgoingDocumentRepository.updateStatusById(outgoingDocumentId, OutgoingDocumentStatusEnum.UNPROCESSED);
          });
        }
      }
    });

    // send email
    // return request: previous = sender, current = receiver
    User currentProcessingUser = returnRequests.get(0).getCurrentProcessingUser();
    User previousProcessingUser = returnRequests.get(0).getPreviousProcessingUser();
    emailService.sendReturnDocumentEmail(previousProcessingUser.getFullName(), currentProcessingUser.getEmail(),
        currentProcessingUser.getFullName(), formatDocIds(returnRequestDto.getDocumentIds()));
    return returnRequests.stream().map(ReturnRequest::getId).toList();
  }

  private void validateUsers(ReturnRequestPostDto returnRequestDto, ReturnRequest returnRequest) {
    // validate current processing user
    User currentProcessingUser = userRepository.findById(
            returnRequestDto.getCurrentProcessingUserId())
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    // validate previous processing user
    if (returnRequestDto.getReturnRequestType().equals(ReturnRequestType.WITHDRAW)) {
      User previousProcessingUser = userRepository.findById(
              returnRequestDto.getPreviousProcessingUserId())
          .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
      returnRequest.setPreviousProcessingUser(previousProcessingUser);
    }

    returnRequest.setCurrentProcessingUser(currentProcessingUser);
  }

  private TransferHistory createTransferHistory(ReturnRequest returnRequest,
      ProcessingDocumentTypeEnum type) {
    User currentUser = SecurityUtils.getCurrentUser();
    TransferHistory transferHistory = new TransferHistory();
    transferHistory.setReturnRequest(returnRequest);
    if (Objects.equals(returnRequest.getCurrentProcessingUser().getId(), currentUser.getId())) {
      transferHistory.setSender(returnRequest.getCurrentProcessingUser());
      transferHistory.setReceiver(returnRequest.getPreviousProcessingUser());
    } else {
      transferHistory.setSender(returnRequest.getPreviousProcessingUser());
      transferHistory.setReceiver(returnRequest.getCurrentProcessingUser());
    }
    transferHistory.setIsRead(false);
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      transferHistory.setIncomingDocumentIds(List.of(returnRequest.getIncomingDocument().getId()));
    } else if (type == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      transferHistory.setOutgoingDocumentIds(List.of(returnRequest.getOutgoingDocument().getId()));
    }
    return transferHistory;
  }
}

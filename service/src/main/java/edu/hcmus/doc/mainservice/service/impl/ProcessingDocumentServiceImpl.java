package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.getStep;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.getStepOutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailCustomResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingDocumentServiceImpl implements ProcessingDocumentService {

    private final ProcessingDocumentRepository processingDocumentRepository;

    private final ProcessingUserRepository processingUserRepository;

    private final UserRepository userRepository;

    private final OutgoingDocumentRepository outgoingDocumentRepository;

    @Override
    public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
        return processingDocumentRepository.getTotalElements(searchCriteriaDto);
    }

    @Override
    public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
        return processingDocumentRepository.getTotalPages(searchCriteriaDto, limit);
    }

    @Override
    public List<ProcessingDocument> searchProcessingDocuments(
            SearchCriteriaDto searchCriteriaDto,
            long offset,
            long limit
    ) {
        return processingDocumentRepository.searchByCriteria(searchCriteriaDto, offset, limit);
    }

    /**
     * Kiem tra xem user co dang xu ly van ban nay khong, neu co => true, nguoc lai => false
     *
     * @param request
     * @return
     */
    @Override
    public Boolean isUserWorkingOnDocumentWithSpecificRole(GetTransferDocumentDetailRequest request) {
        GetTransferDocumentDetailResponse detail = processingDocumentRepository.getTransferDocumentDetail(
                request);
        return detail != null;
    }

    @Override
    public Boolean isUserWorkingOnOutgoingDocumentWithSpecificRole(GetTransferDocumentDetailRequest request) {
        GetTransferDocumentDetailResponse detail = processingDocumentRepository.getTransferOutgoingDocumentDetail(
                request);
        return detail != null;
    }

    @Override
    public ValidateTransferDocDto validateTransferDocument(TransferDocDto transferDocDto) {

        User currentUser = SecurityUtils.getCurrentUser();
        User reporter = userRepository.findById(Objects.requireNonNull(transferDocDto.getReporterId()))
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        User assignee = userRepository.findById(Objects.requireNonNull(transferDocDto.getAssigneeId()))
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        int step = getStep(reporter, assignee, false);

        ValidateTransferDocDto response = new ValidateTransferDocDto();
        response.setMessage("");
        response.setIsValid(true);

        // neu la van thu, khong can kiem tra
        if (currentUser.getRole() == DocSystemRoleEnum.VAN_THU) {
            return response;
        }

        if (transferDocDto.getIsTransferToSameLevel()) {

            // kiem tra xem current user co phai la assignee cua van ban nay khong
            for (Long incomingDocId : Objects.requireNonNull(transferDocDto.getDocumentIds())) {
                Boolean currentUserCheck = isUserWorkingOnDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(incomingDocId)
                                .step(step)
                                .userId(currentUser.getId())
                                .role(ProcessingDocumentRoleEnum.ASSIGNEE)
                                .build()
                );
                if (!currentUserCheck) {
                    Object[] arguments = new Object[]{incomingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_not_have_permission_to_transfer_document_in_same_level, arguments));
                    break;
                }
                // kiem tra xem assignee co dang xu ly van ban nay khong
                Boolean assigneeCheck = isUserWorkingOnDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(incomingDocId)
                                .step(step)
                                .userId(assignee.getId())
                                .role(null)
                                .build()
                );
                if (assigneeCheck) {
                    Object[] arguments = {assignee.getFullName(), incomingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_has_already_exists_in_the_flow_of_document, arguments));
                    break;
                }
            }
        } else {
            List<User> collaborators = userRepository.findAllById(
                    Objects.requireNonNull(transferDocDto.getCollaboratorIds()));
            // current user phai la assignee thi moi duoc phep transfer
            for (Long incomingDocId : Objects.requireNonNull(transferDocDto.getDocumentIds())) {
                Boolean currentUserCheck = isUserWorkingOnDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(incomingDocId)
                                .step(step)
                                .userId(currentUser.getId())
                                .role(ProcessingDocumentRoleEnum.ASSIGNEE)
                                .build()
                );
                if (!currentUserCheck) {
                    Object[] arguments = {incomingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_not_have_permission_to_transfer_this_document, arguments));
                    break;
                }

                // kiem tra xem tai step hien tai, assignee, reporter, collaborator co dang xu ly van ban nay khong
                Boolean assigneeCheck = isUserWorkingOnDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(incomingDocId)
                                .step(step)
                                .userId(assignee.getId())
                                .role(ProcessingDocumentRoleEnum.ASSIGNEE)
                                .build()
                );
                if (assigneeCheck) {
                    Object[] arguments = {assignee.getFullName(), incomingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_has_already_exists_in_the_flow_of_document, arguments));
                    break;
                }
                StringBuilder collaboratorNames = new StringBuilder();
                boolean isCollaboratorsValid = true;
                for (User collaborator : collaborators) {
                    Boolean collaboratorCheck = isUserWorkingOnDocumentWithSpecificRole(
                            GetTransferDocumentDetailRequest.builder()
                                    .documentId(incomingDocId)
                                    .step(step)
                                    .userId(collaborator.getId())
                                    .role(ProcessingDocumentRoleEnum.COLLABORATOR)
                                    .build()
                    );
                    if (collaboratorCheck) {
                        isCollaboratorsValid = false;
                        collaboratorNames.append(collaborator.getFullName()).append(", ");
                    }
                }
                if (!isCollaboratorsValid) {
                    Object[] arguments = {collaboratorNames.substring(0, collaboratorNames.length() - 2)};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_has_already_exists_in_the_flow_of_document, arguments));
                    break;
                }
            }
        }

        return response;
    }

    @Override
    public ValidateTransferDocDto validateTransferOutgoingDocument(TransferDocDto transferDocDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        User reporter = userRepository.findById(Objects.requireNonNull(transferDocDto.getReporterId()))
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        User assignee = userRepository.findById(Objects.requireNonNull(transferDocDto.getAssigneeId()))
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        List<User> collaborators = userRepository.findAllById(Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

        int step = getStepOutgoingDocument(reporter, false);

        ValidateTransferDocDto response = new ValidateTransferDocDto();
        response.setMessage("");
        response.setIsValid(true);

        // neu la van thu, khong duoc phep chuyen van ban
        if (currentUser.getRole() == DocSystemRoleEnum.VAN_THU) {
            Object[] arguments = {currentUser.getUsername()};
            response.setIsValid(false);
            response.setMessage(DocMessageUtils.getContent(MESSAGE.user_not_have_permission_to_transfer_this_document, arguments));
            return response;
        }

        for (Long outgoingDocId : Objects.requireNonNull(transferDocDto.getDocumentIds())) {

            OutgoingDocument outgoingDocument = outgoingDocumentRepository.getOutgoingDocumentById(outgoingDocId);

            if (ObjectUtils.isEmpty(outgoingDocument)) {
                throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
            }

            // current user phai la assignee thi moi duoc phep transfer van ban dang xu ly
            if (OutgoingDocumentStatusEnum.IN_PROGRESS.equals(outgoingDocument.getStatus())) {
                Boolean currentUserCheck = isUserWorkingOnOutgoingDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(outgoingDocId)
                                .step(step)
                                .userId(currentUser.getId())
                                .role(ProcessingDocumentRoleEnum.ASSIGNEE)
                                .build()
                );
                if (!currentUserCheck) {
                    Object[] arguments = {outgoingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_not_have_permission_to_transfer_this_document, arguments));
                    break;
                }

                // kiem tra xem tai step hien tai, assignee, reporter, collaborator co dang xu ly van ban nay khong
                Boolean assigneeCheck = isUserWorkingOnOutgoingDocumentWithSpecificRole(
                        GetTransferDocumentDetailRequest.builder()
                                .documentId(outgoingDocId)
                                .step(step)
                                .userId(assignee.getId())
                                .role(ProcessingDocumentRoleEnum.ASSIGNEE)
                                .build()
                );
                if (assigneeCheck) {
                    Object[] arguments = {assignee.getFullName(), outgoingDocId};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_has_already_exists_in_the_flow_of_document, arguments));
                    break;
                }
                StringBuilder collaboratorNames = new StringBuilder();
                boolean isCollaboratorsValid = true;
                for (User collaborator : collaborators) {
                    Boolean collaboratorCheck = isUserWorkingOnOutgoingDocumentWithSpecificRole(
                            GetTransferDocumentDetailRequest.builder()
                                    .documentId(outgoingDocId)
                                    .step(step)
                                    .userId(collaborator.getId())
                                    .role(ProcessingDocumentRoleEnum.COLLABORATOR)
                                    .build()
                    );
                    if (collaboratorCheck) {
                        isCollaboratorsValid = false;
                        collaboratorNames.append(collaborator.getFullName()).append(", ");
                    }
                }
                if (!isCollaboratorsValid) {
                    Object[] arguments = {collaboratorNames.substring(0, collaboratorNames.length() - 2)};
                    response.setIsValid(false);
                    response.setMessage(DocMessageUtils.getContent(
                            MESSAGE.user_has_already_exists_in_the_flow_of_document, arguments));
                    break;
                }
            } else if (OutgoingDocumentStatusEnum.RELEASED.equals(outgoingDocument.getStatus())) {
                Object[] arguments = {outgoingDocId};
                response.setIsValid(false);
                response.setMessage(DocMessageUtils.getContent(
                        MESSAGE.user_not_have_permission_to_transfer_this_document, arguments));
                break;
            }
            // if not transferable, throw exception
            boolean isTransferable = outgoingDocumentRepository.getOutgoingDocumentsWithTransferPermission()
                .contains(outgoingDocument.getId());
            if (!isTransferable) {
                Object[] arguments = {outgoingDocId};
                response.setIsValid(false);
                response.setMessage(DocMessageUtils.getContent(
                        MESSAGE.user_not_have_permission_to_transfer_this_document, arguments));
                break;
            }
        }

        return response;
    }

    @Override
    public GetTransferDocumentDetailCustomResponse getTransferDocumentDetail(GetTransferDocumentDetailRequest request, ProcessingDocumentType processingDocumentType) {
        User currentUser = SecurityUtils.getCurrentUser();
        GetTransferDocumentDetailCustomResponse response = new GetTransferDocumentDetailCustomResponse();
        GetTransferDocumentDetailResponse baseInfo;

        if (ProcessingDocumentType.INCOMING_DOCUMENT.equals(processingDocumentType)) {
            baseInfo = processingDocumentRepository.getTransferDocumentDetail(request);
        } else {
            baseInfo = processingDocumentRepository.getTransferOutgoingDocumentDetail(request);
        }

        if (Objects.isNull(baseInfo)) {
            throw new ProcessingDocumentNotFoundException(ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND);
        }

        Long assigneeId = processingDocumentRepository.getListOfUserIdRelatedToTransferredDocument(
                baseInfo.getProcessingDocumentId(), baseInfo.getStep(), ProcessingDocumentRoleEnum.ASSIGNEE).get(0);

        List<Long> collaboratorIds = processingDocumentRepository.getListOfUserIdRelatedToTransferredDocument(
                baseInfo.getProcessingDocumentId(), baseInfo.getStep(),
                ProcessingDocumentRoleEnum.COLLABORATOR);

        Long reporterId = processingDocumentRepository.getListOfUserIdRelatedToTransferredDocument(
                baseInfo.getProcessingDocumentId(), baseInfo.getStep(), ProcessingDocumentRoleEnum.REPORTER).get(0);
        User senderUser = userRepository.getUserById(reporterId).orElse(currentUser);

        response.setBaseInfo(baseInfo);
        response.setAssigneeId(assigneeId);
        response.setCollaboratorIds(collaboratorIds);
        response.setSenderName(senderUser.getFullName());

        return response;
    }

    @Override
    public ProcessingStatus getProcessingStatus(Long documentId) {
        return processingDocumentRepository.getProcessingStatus(documentId).orElse(ProcessingStatus.UNPROCESSED);
    }

    @Override
    public Integer getCurrentStep(Long documentId) {
        return processingDocumentRepository.getCurrentStep(documentId).get(0, Integer.class);
    }

    @Override
    public Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId, DocSystemRoleEnum userRole, Boolean isAnyRole) {
        return processingUserRepository.getDateExpired(incomingDocumentId, userId, userRole, isAnyRole);
    }

    @Override
    public Optional<String> getDateExpiredV2(Long documentId, Long userId,
                                             DocSystemRoleEnum userRole, Boolean isAnyRole, ProcessingDocumentTypeEnum type) {
        return processingUserRepository.getDateExpiredV2(documentId, userId, userRole, isAnyRole, type);
    }

    @Override
    public Boolean isExistUserWorkingOnThisDocumentAtSpecificStep(Long documentId, Integer step, ProcessingDocumentTypeEnum processingDocumentType) {
        User currentUser = SecurityUtils.getCurrentUser();
        ProcessingDocument processingDocument;
        if (processingDocumentType.equals(ProcessingDocumentTypeEnum.INCOMING_DOCUMENT)) {
            // neu la luong INCOMING, thi TRUONG_PHONG se check rieng vi CHUYEN_VIEN khong the chuyen xu ly
            if (currentUser.getRole().equals(DocSystemRoleEnum.TRUONG_PHONG)) {
                return processingDocumentRepository.isDocumentClosed(documentId, processingDocumentType);
            } else {
                processingDocument = processingDocumentRepository.findByIncomingDocumentId(documentId)
                        .orElse(null);
            }
        } else {
            // neu la luong OUTGOING, thi ai cung co the release van ban => can check nhu sau
            // 1. neu van ban da release, thi return true de khong cho phep rut lai
            // 2. neu van ban chua release, thi check tiep xem nguoi xu ly sau da xu ly van ban hay chua
            if (outgoingDocumentRepository.isDocumentReleased(documentId)) {
                // neu document da released, thi return ngay lap tuc
                return true;
            } else {
                processingDocument = processingDocumentRepository.findByOutgoingDocumentId(documentId)
                    .orElse(null);
            }
        }
        if (Objects.isNull(processingDocument))
            return false;
        return processingDocumentRepository.isExistUserWorkingOnThisDocumentAtSpecificStep(processingDocument.getId(), step);
    }

}

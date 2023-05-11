package edu.hcmus.doc.mainservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentMenuConfig;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.*;
import edu.hcmus.doc.mainservice.model.enums.*;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserRoleNotFoundException;
import edu.hcmus.doc.mainservice.repository.*;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class IncomingDocumentServiceImpl implements IncomingDocumentService {

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final FolderService folderService;

  private final AttachmentService attachmentService;

  private final ObjectMapper objectMapper;

  private final IncomingDocumentMapper incomingDecoratorDocumentMapper;

  private final AttachmentMapperDecorator attachmentMapperDecorator;

  private final ProcessingDocumentRepository processingDocumentRepository;

  private final ProcessingUserRepository processingUserRepository;

  private final ProcessingUserRoleRepository processingUserRoleRepository;

  private final UserRepository userRepository;

  private final ReturnRequestRepository returnRequestRepository;

  private final FolderRepository folderRepository;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument) {
    IncomingDocument updatingIncomingDocument = getIncomingDocumentById(incomingDocument.getId());
    DocObjectUtils.copyNonNullProperties(incomingDocument, updatingIncomingDocument);
    return incomingDocumentRepository.saveAndFlush(updatingIncomingDocument);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return getTotalElements(searchCriteriaDto) / limit;
  }

  @Override
  public IncomingDocument getIncomingDocumentById(Long id) {
    IncomingDocument incomingDocument = incomingDocumentRepository.getIncomingDocumentById(id);

    if (ObjectUtils.isEmpty(incomingDocument)) {
      throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
    }

    return incomingDocument;
  }

  @Override
  public List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria,
      int page, int pageSize) {
    return processingDocumentRepository.searchByCriteria(searchCriteria, page, pageSize);
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
  }

  @Override
  public IncomingDocument findById(Long id) {
    return incomingDocumentRepository.findById(id).orElseThrow(
        () -> new IncomingDocumentNotFoundException(INCOMING_DOCUMENT_NOT_FOUND)
    );
  }

  @Override
  public IncomingDocument createIncomingDocument(
      IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto)
      throws JsonProcessingException {
    IncomingDocumentPostDto incomingDocumentPostDto = objectMapper.readValue(
        incomingDocumentWithAttachmentPostDto.getIncomingDocumentPostDto(),
        IncomingDocumentPostDto.class);
    IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(
        incomingDocumentPostDto);

    Folder folder = folderService.findById(incomingDocument.getFolder().getId());
    folder.setNextNumber(folder.getNextNumber() + 1);
    IncomingDocument savedIncomingDocument = incomingDocumentRepository.save(incomingDocument);

    AttachmentPostDto attachmentPostDto = attachmentMapperDecorator.toAttachmentPostDto(
        savedIncomingDocument.getId(), incomingDocumentWithAttachmentPostDto.getAttachments());

    attachmentService.saveAttachmentsByIncomingDocId(
        attachmentPostDto);
    return savedIncomingDocument;
  }

  @Override
  public void transferDocuments(TransferDocDto transferDocDto) {
    User currentUser = SecurityUtils.getCurrentUser();
    User reporter = getUserByIdOrThrow(transferDocDto.getReporterId());
    User assignee = getUserByIdOrThrow(transferDocDto.getAssigneeId());

    if (transferDocDto.getIsTransferToSameLevel()) {
      transferToSameLevel(transferDocDto, assignee);
      return;
    }

    List<User> collaborators = userRepository.findAllById(
        Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

    if (transferDocDto.getTransferDocumentType() == TransferDocumentType.TRANSFER_TO_GIAM_DOC
        && currentUser.getRole() == DocSystemRoleEnum.VAN_THU) {
      transferNewDocuments(transferDocDto, reporter, assignee, collaborators);
    } else {
      transferExistedDocuments(transferDocDto, reporter, assignee, collaborators);
    }
  }

  private void transferToSameLevel(TransferDocDto transferDocDto, User assignee) {
    List<IncomingDocument> incomingDocuments = incomingDocumentRepository
        .getIncomingDocumentsByIds(transferDocDto.getDocumentIds());

    // update the created_by field of incoming documents
    incomingDocuments.forEach(incomingDocument -> {
      incomingDocument.setCreatedBy(assignee.getId().toString());
      incomingDocumentRepository.save(incomingDocument);
    });

  }

  private void transferNewDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators) {

    List<IncomingDocument> incomingDocuments = incomingDocumentRepository
        .getIncomingDocumentsByIds(transferDocDto.getDocumentIds());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    // TODO: validate incomingDocuments to make sure they are not processed yet.
    // save processing documents with status IN_PROGRESS
    incomingDocuments.forEach(incomingDocument -> {
      ProcessingDocument processingDocument = createProcessingDocument(incomingDocument,
          ProcessingStatus.IN_PROGRESS);

      ProcessingDocument savedProcessingDocument = processingDocumentRepository.save(
          processingDocument);

      saveCollaboratorList(savedProcessingDocument, collaborators, returnRequest, transferDocDto,
          1);

      saveReporterOrAssignee(savedProcessingDocument, assignee, returnRequest, transferDocDto, 1,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      saveReporterOrAssignee(savedProcessingDocument, reporter, returnRequest, transferDocDto, 1,
          ProcessingDocumentRoleEnum.REPORTER);
    });
  }

  private void transferExistedDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators) {
    List<ProcessingDocument> processingDocuments = processingDocumentRepository.findAllByIds(
        transferDocDto.getDocumentIds());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    processingDocuments.forEach(processingDocument -> {

      saveCollaboratorList(processingDocument, collaborators, returnRequest, transferDocDto, 2);

      saveReporterOrAssignee(processingDocument, assignee, returnRequest, transferDocDto, 2,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      saveReporterOrAssignee(processingDocument, reporter, returnRequest, transferDocDto, 2,
          ProcessingDocumentRoleEnum.REPORTER);
    });
  }

  private void saveCollaboratorList(ProcessingDocument processingDocument, List<User> collaborators,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step) {
    collaborators.forEach(collaborator -> {
      ProcessingUser processingUser1 = createProcessingUser(processingDocument, collaborator, step,
          returnRequest, transferDocDto);
      ProcessingUser savedProcessingUser1 = processingUserRepository.save(processingUser1);

      ProcessingUserRole processingUserRole1 = createProcessingUserRole(savedProcessingUser1,
          ProcessingDocumentRoleEnum.COLLABORATOR);
      processingUserRoleRepository.save(processingUserRole1);
    });
  }

  private void saveReporterOrAssignee(ProcessingDocument processingDocument, User user,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step,
      ProcessingDocumentRoleEnum role) {
    ProcessingUser processingUser = createProcessingUser(processingDocument, user, step,
        returnRequest, transferDocDto);
    ProcessingUser savedProcessingUser = processingUserRepository.save(processingUser);

    ProcessingUserRole processingUserRole = createProcessingUserRole(savedProcessingUser, role);
    processingUserRoleRepository.save(processingUserRole);
  }

  private User getUserByIdOrThrow(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  private ProcessingDocument createProcessingDocument(IncomingDocument incomingDocument,
      ProcessingStatus processingStatus) {
    ProcessingDocument processingDocument = new ProcessingDocument();
    processingDocument.setIncomingDoc(incomingDocument);
    processingDocument.setStatus(processingStatus);
    processingDocument.setOpened(true);
    processingDocument.setProcessingRequest("processing_request");
    return processingDocument;
  }

  private ProcessingUser createProcessingUser(ProcessingDocument processingDocument, User user,
      Integer step, ReturnRequest returnRequest, TransferDocDto transferDocDto) {
    ProcessingUser processingUser = new ProcessingUser();
    processingUser.setProcessingDocument(processingDocument);
    processingUser.setUser(user);
    processingUser.setStep(step);
    processingUser.setReturnRequest(returnRequest);
    processingUser.setProcessMethod(transferDocDto.getProcessMethod());

    if (Boolean.FALSE.equals(transferDocDto.getIsInfiniteProcessingTime())) {
      processingUser.setProcessingDuration(LocalDate.parse(
          Objects.requireNonNull(transferDocDto.getProcessingTime()),
          DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    return processingUser;
  }

  private ProcessingUserRole createProcessingUserRole(ProcessingUser processingUser,
      ProcessingDocumentRoleEnum role) {
    ProcessingUserRole processingUserRole = new ProcessingUserRole();
    processingUserRole.setProcessingUser(processingUser);
    processingUserRole.setRole(role);

    return processingUserRole;
  }

  @Override
  public TransferDocumentModalSettingDto getTransferDocumentModalSetting() {
    TransferDocumentModalSettingDto settings = new TransferDocumentModalSettingDto();
    List<TransferDocumentMenuConfig> menuConfigs = new ArrayList<>();
    User currUser = SecurityUtils.getCurrentUser();
    settings.setCurrentRole(currUser.getRole());
    // TODO: i18n for backend response
    switch (currUser.getRole()) {
      case VAN_THU -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("Trình văn bản lên ban Giám Đốc")
            .component(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel("Ban giám đốc")
            .menuKey(1)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("luân chuyển văn bản tới văn thư")
            .component(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .menuLabel("Văn thư")
            .menuKey(3)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU)
            .isTransferToSameLevel(true)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC);
        settings.setDefaultComponent(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value);
      }
      case GIAM_DOC -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("phân công văn bản cho trưởng phòng")
            .component(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel("Chánh văn phòng")
            .menuKey(2)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG);
        settings.setDefaultComponent(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value);
      }
      case TRUONG_PHONG -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("Trình văn bản lên ban Giám Đốc")
            .component(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel("Ban giám đốc")
            .menuKey(1)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("phân công văn bản cho chuyên viên")
            .component(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value)
            .menuLabel("Chuyên viên")
            .menuKey(4)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_CHUYEN_VIEN)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC);
        settings.setDefaultComponent(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value);
      }
      case CHUYEN_VIEN -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("Trình văn bản lên ban trưởng phòng")
            .component(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel("Chánh văn phòng")
            .menuKey(2)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel("luân chuyển văn bản tới văn thư")
            .component(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .menuLabel("Văn thư")
            .menuKey(3)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG);
        settings.setDefaultComponent(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value);
      }
      default -> {
        throw new UserRoleNotFoundException(UserRoleNotFoundException.USER_ROLE_NOT_FOUND);
      }
    }
    settings.setMenuConfigs(menuConfigs);
    return settings;
  }
}

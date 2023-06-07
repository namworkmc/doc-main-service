package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.enums.MESSAGE.user_has_already_exists_in_the_flow_of_document;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.createProcessingDocument;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.getStepOutgoingDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentMenuConfig;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentComponent;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentType;
import edu.hcmus.doc.mainservice.model.exception.DocStatusViolatedException;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.TransferDocumentException;
import edu.hcmus.doc.mainservice.model.exception.UserRoleNotFoundException;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import edu.hcmus.doc.mainservice.util.ResourceBundleUtils;
import edu.hcmus.doc.mainservice.util.mapper.OutgoingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class OutgoingDocumentServiceImpl implements OutgoingDocumentService {
  private final OutgoingDocumentRepository outgoingDocumentRepository;
  private final OutgoingDocumentMapper outgoingDecoratorDocumentMapper;
  private final AttachmentMapperDecorator attachmentMapperDecorator;
  private final AttachmentService attachmentService;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final ReturnRequestRepository returnRequestRepository;
  private final ProcessingDocumentRepository processingDocumentRepository;
  private final IncomingDocumentService incomingDocumentService;
  private final ProcessingDocumentService processingDocumentService;


  @Override
  public OutgoingDocument getOutgoingDocumentById(Long id) {
    OutgoingDocument outgoingDocument = outgoingDocumentRepository.getOutgoingDocumentById(id);

    if (ObjectUtils.isEmpty(outgoingDocument)) {
      throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
    }

    return outgoingDocument;
  }

  @Override
  public OutgoingDocument releaseDocument(OutgoingDocument document) {
    OutgoingDocument updatingDocument = getOutgoingDocumentById(document.getId());
    DocObjectUtils.copyNonNullProperties(document, updatingDocument);

    if (updatingDocument.getStatus() != OutgoingDocumentStatusEnum.RELEASED) {
      updatingDocument.setStatus(OutgoingDocumentStatusEnum.RELEASED);
    } else {
      throw new DocStatusViolatedException(DocStatusViolatedException.STATUS_VIOLATED);
    }

    return outgoingDocumentRepository.saveAndFlush(updatingDocument);
  }

  @Override
  public OutgoingDocument createOutgoingDocument(OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto)
          throws JsonProcessingException {
    OutgoingDocumentPostDto outgoingDocumentPostDto =
            objectMapper.readValue(
                    outgoingDocumentWithAttachmentPostDto.getOutgoingDocumentPostDto(),
            OutgoingDocumentPostDto.class);

    OutgoingDocument outgoingDocument = outgoingDecoratorDocumentMapper
            .toEntity(outgoingDocumentPostDto);

    OutgoingDocument savedOutgoingDocument = outgoingDocumentRepository.save(outgoingDocument);

    if(Objects.nonNull(outgoingDocumentWithAttachmentPostDto.getAttachments())) {
      AttachmentPostDto attachmentPostDto = attachmentMapperDecorator.toAttachmentPostDto(
          savedOutgoingDocument.getId(), outgoingDocumentWithAttachmentPostDto.getAttachments());

      attachmentService.saveAttachmentsByProcessingDocumentTypeAndDocId(ParentFolderEnum.OGD, attachmentPostDto);
    }

    return savedOutgoingDocument;
  }

  @Override
  public OutgoingDocument updateOutgoingDocument(OutgoingDocument outgoingDocument) {
    OutgoingDocument updatingDocument = getOutgoingDocumentById(outgoingDocument.getId());
    DocObjectUtils.copyNonNullProperties(outgoingDocument, updatingDocument);

    if (updatingDocument.getStatus() == OutgoingDocumentStatusEnum.RELEASED) {
      throw new DocStatusViolatedException(DocStatusViolatedException.STATUS_VIOLATED);
    }

    return outgoingDocumentRepository.saveAndFlush(updatingDocument);
  }

  @Override
  public long getTotalElements(OutgoingDocSearchCriteriaDto searchCriteriaDto){
    return outgoingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public long getTotalPages(OutgoingDocSearchCriteriaDto searchCriteriaDto, long limit){
    return outgoingDocumentRepository.getTotalPages(searchCriteriaDto, limit);
  }

  @Override
  public List<OutgoingDocument> searchOutgoingDocuments(OutgoingDocSearchCriteriaDto searchCriteria, int page, int pageSize) {
    return outgoingDocumentRepository.searchByCriteria(searchCriteria, page, pageSize);
  }

  @Override
  public TransferDocumentModalSettingDto getTransferOutgoingDocumentModalSetting() {
    TransferDocumentModalSettingDto settings = new TransferDocumentModalSettingDto();
    List<TransferDocumentMenuConfig> menuConfigs = new ArrayList<>();
    User currUser = SecurityUtils.getCurrentUser();
    settings.setCurrentRole(currUser.getRole());

    switch (currUser.getRole()) {
      case CHUYEN_VIEN -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value);
      }
      case TRUONG_PHONG -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(true)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value);
      }
      case GIAM_DOC -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(true)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value);
      }
      case VAN_THU -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value);
      }
      default -> throw new UserRoleNotFoundException(UserRoleNotFoundException.USER_ROLE_NOT_FOUND);
    }
    settings.setMenuConfigs(menuConfigs);
    return settings;
  }

  @Override
  public void transferDocuments(TransferDocDto transferDocDto) {
    ValidateTransferDocDto validateTransferDocDto = processingDocumentService.validateTransferOutgoingDocument(transferDocDto);
    if (!validateTransferDocDto.getIsValid()) {
      throw new TransferDocumentException(validateTransferDocDto.getMessage());
    }

    User reporter = incomingDocumentService.getUserByIdOrThrow(transferDocDto.getReporterId());
    User assignee = incomingDocumentService.getUserByIdOrThrow(transferDocDto.getAssigneeId());

    List<User> collaborators = userRepository.findAllById(
        Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

    List<OutgoingDocument> outgoingDocuments = outgoingDocumentRepository
        .getOutgoingDocumentsByIds(transferDocDto.getDocumentIds());

    List<OutgoingDocument> unprocessedOutgoingDocuments = outgoingDocuments.stream()
        .filter(outgoingDocument -> OutgoingDocumentStatusEnum.UNPROCESSED.equals(outgoingDocument.getStatus()))
        .toList();

    List<OutgoingDocument> inprogressOutgoingDocuments = outgoingDocuments.stream()
        .filter(outgoingDocument -> OutgoingDocumentStatusEnum.IN_PROGRESS.equals(outgoingDocument.getStatus()))
        .toList();

    if (!unprocessedOutgoingDocuments.isEmpty() && !inprogressOutgoingDocuments.isEmpty()) {
      transferNewDocuments(transferDocDto, reporter, assignee, collaborators, unprocessedOutgoingDocuments);
      transferExistedDocuments(transferDocDto, reporter, assignee, collaborators, inprogressOutgoingDocuments);
    } else {
      if(unprocessedOutgoingDocuments.isEmpty()){
        transferExistedDocuments(transferDocDto, reporter, assignee, collaborators, outgoingDocuments);
      } else {
        transferNewDocuments(transferDocDto, reporter, assignee, collaborators, outgoingDocuments);
      }
    }
  }

  private void transferNewDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators, List<OutgoingDocument> outgoingDocuments) {
    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    int step = getStepOutgoingDocument(reporter, true);

    // save processing documents with status IN_PROGRESS
    outgoingDocuments.forEach(outgoingDocument -> {
      ProcessingDocument processingDocument = createProcessingDocument(null, outgoingDocument, ProcessingStatus.IN_PROGRESS);

      ProcessingDocument savedProcessingDocument = processingDocumentRepository.save(
          processingDocument);

      incomingDocumentService.saveCollaboratorList(savedProcessingDocument, collaborators, returnRequest, transferDocDto,
          step);

      incomingDocumentService.saveReporterOrAssignee(savedProcessingDocument, assignee, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      incomingDocumentService.saveReporterOrAssignee(savedProcessingDocument, reporter, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.REPORTER);

      outgoingDocument.setStatus(OutgoingDocumentStatusEnum.IN_PROGRESS);
      outgoingDocumentRepository.saveAndFlush(outgoingDocument);
    });
  }

  private void transferExistedDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators, List<OutgoingDocument> outgoingDocuments) {
    int step = getStepOutgoingDocument(reporter, true);
    List<ProcessingDocument> processingDocuments = processingDocumentRepository.findAllOutgoingByIds(outgoingDocuments.stream()
        .map(OutgoingDocument::getId)
        .toList());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    processingDocuments.forEach(processingDocument -> {
      if(processingDocumentService.getCurrentStep(processingDocument.getId()) >= step) {
        throw new TransferDocumentException(user_has_already_exists_in_the_flow_of_document.toString());
      }

      incomingDocumentService.saveCollaboratorList(processingDocument, collaborators, returnRequest, transferDocDto, step);

      incomingDocumentService.saveReporterOrAssignee(processingDocument, assignee, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      incomingDocumentService.saveReporterOrAssignee(processingDocument, reporter, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.REPORTER);
    });
  }
}

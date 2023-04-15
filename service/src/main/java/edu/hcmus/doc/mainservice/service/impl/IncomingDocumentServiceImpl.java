package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.FolderNotFoundException;
import edu.hcmus.doc.mainservice.repository.FolderRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;

import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;

import java.util.Objects;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public IncomingDocument createIncomingDocument(IncomingDocument incomingDocument) {
        Folder folder = folderRepository.findById(incomingDocument.getFolder().getId())
                .orElseThrow(() -> new FolderNotFoundException(FolderNotFoundException.FOLDER_NOT_FOUND));
        folder.setNextNumber(folder.getNextNumber() + 1);

    return incomingDocumentRepository.save(incomingDocument);
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
  public List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria, int page, int pageSize) {
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

  @SneakyThrows
  @Override
  public IncomingDocument createIncomingDocument(
      IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto) {
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
  public void transferDocumentsToDirector(TransferDocDto transferDocDto) {
    User reporter = userRepository.findById(Objects.requireNonNull(transferDocDto.getReporterId()))
        .orElseThrow(
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND)
        );

    User assignee = userRepository.findById(Objects.requireNonNull(transferDocDto.getAssigneeId()))
        .orElseThrow(
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND)
        );

    List<User> collaborators = userRepository.findAllById(
        Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

    List<IncomingDocument> incomingDocuments = incomingDocumentRepository
        .getIncomingDocumentsByIds(transferDocDto.getDocumentIds());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    // TODO: validate incomingDocuments to make sure they are not processed yet.
    // save processing documents with status IN_PROGRESS
    incomingDocuments.forEach(incomingDocument -> {
      ProcessingDocument processingDocument = new ProcessingDocument();
      processingDocument.setIncomingDoc(incomingDocument);
      processingDocument.setStatus(ProcessingStatus.IN_PROGRESS);
      processingDocument.setOpened(true);
      processingDocument.setProcessingRequest("processing_request");

      ProcessingDocument savedProcessingDocument = processingDocumentRepository.save(
          processingDocument);

      collaborators.forEach(collaborator -> {
        ProcessingUser processingUser1 = new ProcessingUser();
        processingUser1.setProcessingDocument(savedProcessingDocument);
        processingUser1.setUser(collaborator);
        processingUser1.setStep(1);
        processingUser1.setReturnRequest(returnRequest);

        ProcessingUser savedProcessingUser1 = processingUserRepository.save(processingUser1);

        ProcessingUserRole processingUserRole1 = new ProcessingUserRole();
        processingUserRole1.setProcessingUser(savedProcessingUser1);
        processingUserRole1.setRole(ProcessingDocumentRoleEnum.COLLABORATOR);

        processingUserRoleRepository.save(processingUserRole1);
      });

      ProcessingUser processingUser2 = new ProcessingUser();
      processingUser2.setProcessingDocument(savedProcessingDocument);
      processingUser2.setUser(assignee);
      processingUser2.setStep(1);
      processingUser2.setReturnRequest(returnRequest);

      ProcessingUser processingUser3 = new ProcessingUser();
      processingUser3.setProcessingDocument(savedProcessingDocument);
      processingUser3.setUser(reporter);
      processingUser3.setStep(1);
      processingUser3.setReturnRequest(returnRequest);

      ProcessingUser savedProcessingUser2 = processingUserRepository.save(processingUser2);
      ProcessingUser savedProcessingUser3 = processingUserRepository.save(processingUser3);

      ProcessingUserRole processingUserRole2 = new ProcessingUserRole();
      processingUserRole2.setProcessingUser(savedProcessingUser2);
      processingUserRole2.setRole(ProcessingDocumentRoleEnum.ASSIGNEE);

      ProcessingUserRole processingUserRole3 = new ProcessingUserRole();
      processingUserRole3.setProcessingUser(savedProcessingUser3);
      processingUserRole3.setRole(ProcessingDocumentRoleEnum.REPORTER);

      processingUserRoleRepository.save(processingUserRole2);
      processingUserRoleRepository.save(processingUserRole3);
    });
  }
}

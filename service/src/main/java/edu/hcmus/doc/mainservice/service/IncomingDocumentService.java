package edu.hcmus.doc.mainservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.StatisticsWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.*;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import java.util.List;

public interface IncomingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria, int page, int pageSize);

  @Deprecated(since = "1.0.0", forRemoval = true)
  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument getIncomingDocumentById(Long id);

  IncomingDocument findById(Long id);

  IncomingDocument createIncomingDocument(IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto)
      throws JsonProcessingException;

  IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument);

  void transferDocuments(TransferDocDto transferDocDto);

  TransferDocumentModalSettingDto getTransferDocumentModalSetting();

  StatisticsWrapperDto getCurrentUserStatistics();

  String closeDocument(Long incomingDocumentId);

  void saveCollaboratorList(ProcessingDocument processingDocument, List<User> collaborators,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step);

  void saveReporterOrAssignee(ProcessingDocument processingDocument, User user,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step,
      ProcessingDocumentRoleEnum role);

  User getUserByIdOrThrow(Long userId);

  void linkDocuments(Long targetDocumentId, List<OutgoingDocumentGetDto> outgoingDocuments);

  List<OutgoingDocument> getLinkedDocuments(Long sourceDocumentId);

  void updateLinkedDocuments(Long targetDocumentId, List<OutgoingDocumentGetDto> documents);

  void deleteLinkedDocuments(Long targetDocumentId, Long linkedDocumentId);
}

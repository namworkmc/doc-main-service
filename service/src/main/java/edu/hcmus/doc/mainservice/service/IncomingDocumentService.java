package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;

public interface IncomingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  IncomingDocument createIncomingDocument(IncomingDocument incomingDocument);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria, int page, int pageSize);

  @Deprecated(since = "1.0.0", forRemoval = true)
  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument getIncomingDocumentById(Long id);

  IncomingDocument findById(Long id);

  IncomingDocument createIncomingDocument(IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto);

  IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument);

  void transferDocuments(TransferDocDto transferDocDto);

  TransferDocumentModalSettingDto getTransferDocumentModalSetting();
}

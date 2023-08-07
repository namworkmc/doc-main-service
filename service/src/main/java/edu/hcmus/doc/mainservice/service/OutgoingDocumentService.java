package edu.hcmus.doc.mainservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import java.util.List;

public interface OutgoingDocumentService {
  OutgoingDocument getOutgoingDocumentById(Long id);

  OutgoingDocument releaseDocument(OutgoingDocument document);

  Boolean validateReleaseDocument(OutgoingDocument outgoingDocument);

  OutgoingDocument createOutgoingDocument(OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto)
          throws JsonProcessingException;

  OutgoingDocument updateOutgoingDocument(OutgoingDocumentWithAttachmentPutDto outgoingDocumentWithAttachmentPutDto)
      throws JsonProcessingException;

  long getTotalElements(OutgoingDocSearchCriteriaDto searchCriteriaDto);

  long getTotalPages(OutgoingDocSearchCriteriaDto searchCriteriaDto, long limit);

  OutgoingDocumentWrapperDto searchOutgoingDocuments(OutgoingDocSearchCriteriaDto searchCriteria, int page, int pageSize);

  TransferDocumentModalSettingDto getTransferOutgoingDocumentModalSetting();

  void transferDocuments(TransferDocDto transferDocDto);

  void linkDocuments(Long targetDocumentId, List<Long> documents);

  List<IncomingDocument> getLinkedDocuments(Long targetDocumentId);

  void deleteLinkedDocuments(Long targetDocumentId, Long linkedDocumentId);
}

package edu.hcmus.doc.mainservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import edu.hcmus.doc.mainservice.util.mapper.OutgoingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;
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

  @Override
  public OutgoingDocument getOutgoingDocumentById(Long id) {
    OutgoingDocument outgoingDocument = outgoingDocumentRepository.getOutgoingDocumentById(id);

    if (ObjectUtils.isEmpty(outgoingDocument)) {
      throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
    }

    return outgoingDocument;
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

    AttachmentPostDto attachmentPostDto = attachmentMapperDecorator.toAttachmentPostDto(
            savedOutgoingDocument.getId(), outgoingDocumentWithAttachmentPostDto.getAttachments());

    attachmentService.saveAttachmentsByOutgoingDocId(attachmentPostDto);

    return savedOutgoingDocument;
  }

  @Override
  public OutgoingDocument updateOutgoingDocument(OutgoingDocument outgoingDocument) {
    OutgoingDocument updatingDocument = getOutgoingDocumentById(outgoingDocument.getId());
    DocObjectUtils.copyNonNullProperties(outgoingDocument, updatingDocument);
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
}

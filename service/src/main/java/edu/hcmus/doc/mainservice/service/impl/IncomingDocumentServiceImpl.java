package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;
import lombok.RequiredArgsConstructor;
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


  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return incomingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @SneakyThrows
  @Override
  public IncomingDocument createIncomingDocument(IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto) {
    IncomingDocumentPostDto incomingDocumentPostDto = objectMapper.readValue(
        incomingDocumentWithAttachmentPostDto.getIncomingDocumentPostDto(), IncomingDocumentPostDto.class);
    IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(
        incomingDocumentPostDto);

    Folder folder = folderService.findById(incomingDocument.getFolder().getId());
    folder.setNextNumber(folder.getNextNumber() + 1);
    IncomingDocument savedIncomingDocument = incomingDocumentRepository.save(incomingDocument);

    AttachmentPostDto attachmentPostDto = attachmentMapperDecorator.toAttachmentPostDto(
        savedIncomingDocument.getId(), incomingDocumentWithAttachmentPostDto.getAttachments());

    List<AttachmentDto> attachmentDtos = attachmentService.saveAttachmentsByIncomingDocId(
        attachmentPostDto);
    return savedIncomingDocument;
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return getTotalElements(searchCriteriaDto) / limit;
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
}

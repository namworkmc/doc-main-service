package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;
import static edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException.OUTGOING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.DocumentWithAttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.AttachmentRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class AttachmentServiceImpl implements AttachmentService {

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.attachment-routing-key}")
  private String routingKey;

  private final AttachmentRepository attachmentRepository;

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final OutgoingDocumentRepository outgoingDocumentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  private final AttachmentMapperDecorator attachmentMapperDecorator;

  @Override
  public List<AttachmentDto> getAttachmentsByDocId(Long docId, ParentFolderEnum parentFolder) {
    return attachmentRepository.getAttachmentsByDocId(docId, parentFolder).stream().map(
        attachmentMapperDecorator::toDto).toList();
  }

  @Override
  public List<DocumentWithAttachmentDto> getDocumentsWithAttachmentsByDocIds(List<Long> docIds,
      ParentFolderEnum parentFolder) {
    List<DocumentWithAttachmentDto> result = new ArrayList<>();
    docIds.forEach(docId -> {
      if (parentFolder == ParentFolderEnum.ICD) {
        incomingDocumentRepository.findById(docId)
            .orElseThrow(() -> new DocumentNotFoundException(INCOMING_DOCUMENT_NOT_FOUND));
      } else {
        outgoingDocumentRepository.findById(docId)
            .orElseThrow(() -> new DocumentNotFoundException(OUTGOING_DOCUMENT_NOT_FOUND));
      }

      List<AttachmentDto> attachments = getAttachmentsByDocId(docId, parentFolder);
      DocumentWithAttachmentDto documentWithAttachmentDto = new DocumentWithAttachmentDto();
      documentWithAttachmentDto.setDocId(docId);
      documentWithAttachmentDto.setAttachments(attachments);
      result.add(documentWithAttachmentDto);
    });
    return result;
  }

  @SneakyThrows
  @Override
  public void saveAttachmentsByProcessingDocumentTypeAndDocId(
      ParentFolderEnum parentFolder, AttachmentPostDto attachmentPostDto) {
    if (CollectionUtils.isEmpty(attachmentPostDto.getAttachments())) {
      return;
    }

    attachmentPostDto.setParentFolder(parentFolder);
    RabbitConverterFuture<List<FileDto>> rabbitConverterFuture = asyncRabbitTemplate
        .convertSendAndReceiveAsType(exchange, routingKey, attachmentPostDto,
            new ParameterizedTypeReference<>() {
            }
        );

    rabbitConverterFuture.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            log.error("Error when saving attachments", ex);
            throw new DocMainServiceRuntimeException("Error when saving attachments", ex);
          }

          @Override
          public void onSuccess(List<FileDto> result) {
            log.info("Save attachments successfully");
          }
        }
    );

    // save file info to db
    List<FileDto> fileDtos = rabbitConverterFuture.get();

    List<AttachmentDto> attachmentDtos = Objects.requireNonNull(fileDtos).stream()
        .map(fileDto -> attachmentMapperDecorator.convertFileDtoToAttachmentDto(
            attachmentPostDto.getDocId(), fileDto)).toList();

    if (parentFolder == ParentFolderEnum.ICD) {
      incomingDocumentRepository.findById(attachmentPostDto.getDocId())
          .ifPresent(incomingDoc ->
              attachmentDtos.stream().map(attachmentMapperDecorator::toEntity)
                  .forEach(attachment -> {
                    attachment.setIncomingDoc(incomingDoc);
                    attachmentRepository.save(attachment);
                  }));
    } else {
      outgoingDocumentRepository.findById(attachmentPostDto.getDocId())
          .ifPresent(doc -> attachmentDtos.stream().map(attachmentMapperDecorator::toEntity)
              .forEach(attachment -> {
                attachment.setOutgoingDocument(doc);
                attachmentRepository.save(attachment);
              }));
    }

  }
}

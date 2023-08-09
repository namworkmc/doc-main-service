package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetListDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.PublishDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailCustomResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.exception.DocBusinessException;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/outgoing-documents")
public class OutgoingDocumentController extends DocAbstractController {
  private final OutgoingDocumentService outgoingDocumentService;
  private final ProcessingDocumentService processingDocumentService;

  @GetMapping("/{id}")
  public OutgoingDocumentGetDto getOutgoingDocument(@PathVariable Long id) {
    return outgoingDecoratorDocumentMapper
            .toDto(outgoingDocumentService.getOutgoingDocumentById(id));
  }
  @SneakyThrows
  @PutMapping("/update")
  public OutgoingDocumentGetDto updateOutgoingDocument(
      @ModelAttribute @Valid OutgoingDocumentWithAttachmentPutDto outgoingDocumentWithAttachmentPutDto) {
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.updateOutgoingDocument(outgoingDocumentWithAttachmentPutDto));
  }

  @PostMapping("/release")
  public OutgoingDocumentGetDto updateOutgoingDocument(
          @RequestBody PublishDocumentDto outgoingDocumentPutDto) {
    OutgoingDocument outgoingDocument = outgoingDecoratorDocumentMapper.toEntity(
            outgoingDocumentPutDto);
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.releaseDocument(outgoingDocument));
  }

  @SneakyThrows
  @PostMapping("/create")
  public OutgoingDocumentGetDto createIncomingDocument(
          @ModelAttribute @Valid OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto) {
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.createOutgoingDocument(outgoingDocumentWithAttachmentPostDto));
  }

  @PostMapping("/search")
  public DocPaginationDto<OutgoingDocumentGetListDto> getOutgoingDocuments(
      @RequestBody OutgoingDocSearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {
    OutgoingDocumentWrapperDto outgoingDocumentWrapperDto = outgoingDocumentService.searchOutgoingDocuments(searchCriteria, page, pageSize);
    return paginationMapper.toDto(
        outgoingDocumentWrapperDto.getOutgoingDocumentGetListDto(),
        outgoingDocumentWrapperDto.getTotalElements(),
        outgoingDocumentWrapperDto.getTotalPages());
  }

  @PostMapping("/search/all")
  public List<OutgoingDocumentGetListDto> getAllOutgoingDocuments(@RequestBody OutgoingDocSearchCriteriaDto searchCriteria) {
    return outgoingDocumentService.searchAllOutgoingDocuments(searchCriteria);
  }

  @GetMapping("/transfer-outgoing-documents-setting")
  public TransferDocumentModalSettingDto getTransferOutgoingDocumentModalSetting() {
    return outgoingDocumentService.getTransferOutgoingDocumentModalSetting();
  }

  @PostMapping("/validate-transfer-documents")
  public ValidateTransferDocDto validateTransferDocuments(@RequestBody TransferDocDto transferDocDto) {
    return processingDocumentService.validateTransferOutgoingDocument(transferDocDto);
  }

  @PostMapping("/transfer-documents")
  public void transferDocuments(@RequestBody TransferDocDto transferDocDto) {
    outgoingDocumentService.transferDocuments(transferDocDto);
  }

  @PostMapping("/get-transfer-documents-detail")
  public GetTransferDocumentDetailCustomResponse getTransferDocumentsDetail(@RequestBody GetTransferDocumentDetailRequest request) {
    return processingDocumentService.getTransferDocumentDetail(request, ProcessingDocumentType.OUTGOING_DOCUMENT);
  }

  @PostMapping("/link-documents/{targetDocumentId}")
  public void linkDocuments(@PathVariable Long targetDocumentId,
                            @RequestBody List<Long> documents) {
    if (documents.isEmpty()) {
      throw new DocBusinessException(DocBusinessException.DOCUMENT_REQUIRED);
    }

    outgoingDocumentService.linkDocuments(targetDocumentId, documents);
  }

  @GetMapping("/link-documents/{targetDocumentId}")
  public List<IncomingDocumentDto> getLinkedDocuments(@PathVariable Long targetDocumentId) {
    return outgoingDocumentService
        .getLinkedDocuments(targetDocumentId)
        .stream()
        .map(incomingDecoratorDocumentMapper::toDto)
        .toList();
  }

  @DeleteMapping("/link-documents/{targetDocumentId}")
  public void deleteLinkedDocuments(@PathVariable Long targetDocumentId,
                                    @RequestParam Long linkedDocumentId) {
    outgoingDocumentService.deleteLinkedDocuments(targetDocumentId, linkedDocumentId);
  }
}

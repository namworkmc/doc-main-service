package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.PublishDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailCustomResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

  @PutMapping("/update")
  public OutgoingDocumentGetDto updateOutgoingDocument(
          @RequestBody OutgoingDocumentPutDto outgoingDocumentPutDto) {
    OutgoingDocument outgoingDocument = outgoingDecoratorDocumentMapper.toEntity(
            outgoingDocumentPutDto);
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.updateOutgoingDocument(outgoingDocument));
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
  @PreAuthorize("hasAuthority('CHUYEN_VIEN')")
  public OutgoingDocumentGetDto createIncomingDocument(
          @ModelAttribute OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto) {
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.createOutgoingDocument(outgoingDocumentWithAttachmentPostDto));
  }

  @PostMapping("/search")
  public DocPaginationDto<OutgoingDocumentGetDto> getOutgoingDocuments(
      @RequestBody(required = false) OutgoingDocSearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {
    return paginationMapper.toDto(
        outgoingDocumentService
            .searchOutgoingDocuments(searchCriteria, page, pageSize)
            .stream()
            .map(outgoingDecoratorDocumentMapper::toDto)
            .toList(),
        outgoingDocumentService.getTotalElements(searchCriteria),
        outgoingDocumentService.getTotalPages(searchCriteria, pageSize));
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
}

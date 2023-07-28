package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPutDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.StatisticsWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailCustomResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingUserRoleService;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

  private final ProcessingDocumentService processingDocumentService;
  private final IncomingDocumentService incomingDocumentService;
  private final ProcessingUserRoleService processingUserRoleService;

  @GetMapping("/{id}")
  public IncomingDocumentDto getIncomingDocument(@PathVariable Long id) {
    return incomingDecoratorDocumentMapper.toDto(incomingDocumentService.getIncomingDocumentById(id));
  }

  @GetMapping("/{processingDocumentType}/{documentId}/processing-details")
  public List<ProcessingDetailsDto> getProcessingDetails(
      @PathVariable ProcessingDocumentTypeEnum processingDocumentType,
      @PathVariable Long documentId,
      @RequestParam(required = false) boolean onlyAssignee) {
    return processingUserRoleService.getProcessingUserRolesByDocumentId(processingDocumentType, documentId, onlyAssignee);
  }

  @PostMapping("/search")
  public DocPaginationDto<IncomingDocumentDto> getIncomingDocuments(
      @RequestBody(required = false) SearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {
    return paginationMapper.toDto(
        incomingDocumentService
            .searchIncomingDocuments(searchCriteria, page, pageSize)
            .stream()
            .map(incomingDecoratorDocumentMapper::toDto)
            .toList(),
        incomingDocumentService.getTotalElements(searchCriteria),
        incomingDocumentService.getTotalPages(searchCriteria, pageSize));
  }

  @SneakyThrows
  @PostMapping("/create")
  public IncomingDocumentDto createIncomingDocument(
      @ModelAttribute IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto) {
    return incomingDecoratorDocumentMapper.toDto(
        incomingDocumentService.createIncomingDocument(incomingDocumentWithAttachmentPostDto));
  }

  @PostMapping("/elastic/search")
  public DocPaginationDto<IncomingDocumentDto> getIncomingDocumentsByElasticSearch(
      @RequestBody ElasticSearchCriteriaDto elasticSearchCriteriaDto,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) throws ExecutionException, InterruptedException {
    ProcessingDocumentSearchResultDto processingDocumentSearchResultDto = processingDocumentService.searchProcessingDocumentsByElasticSearch(
        elasticSearchCriteriaDto, page, pageSize);
    return paginationMapper.toDto(
        processingDocumentSearchResultDto.getProcessingDocuments()
            .stream()
            .map(incomingDecoratorDocumentMapper::toDto)
            .toList(),
        processingDocumentSearchResultDto.getTotalElements(),
        processingDocumentSearchResultDto.getTotalPages());
  }

  @PostMapping("/transfer-documents")
  public void transferDocuments(@RequestBody TransferDocDto transferDocDto) {
    incomingDocumentService.transferDocuments(transferDocDto);
  }

  @SneakyThrows
  @PutMapping("/update")
  public IncomingDocumentDto updateIncomingDocument(
      @ModelAttribute IncomingDocumentWithAttachmentPutDto incomingDocumentWithAttachmentPutDto) {
    return incomingDecoratorDocumentMapper.toDto(
        incomingDocumentService.updateIncomingDocument(incomingDocumentWithAttachmentPutDto));
  }

  @GetMapping("/transfer-documents-setting")
  public TransferDocumentModalSettingDto getTransferDocumentModalSetting() {
    return incomingDocumentService.getTransferDocumentModalSetting();
  }

  @PostMapping("/is-user-working-on-document-with-specific-role")
  public Boolean isUserWorkingOnDocumentWithSpecificRole(@RequestBody GetTransferDocumentDetailRequest request) {
    return processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(request);
  }

  @PostMapping("/validate-transfer-documents")
  public ValidateTransferDocDto validateTransferDocuments(@RequestBody TransferDocDto transferDocDto) {
    return processingDocumentService.validateTransferDocument(transferDocDto);
  }

  @GetMapping("/statistics")
  public StatisticsWrapperDto getStatistics() {
    return incomingDocumentService.getCurrentUserStatistics();
  }

  @PostMapping("/get-transfer-documents-detail")
  public GetTransferDocumentDetailCustomResponse getTransferDocumentsDetail(@RequestBody GetTransferDocumentDetailRequest request) {
    return processingDocumentService.getTransferDocumentDetail(request, ProcessingDocumentType.INCOMING_DOCUMENT);
  }

  @PutMapping("/close-document/{id}")
  public String closeDocument(@PathVariable Long id) {
    return incomingDocumentService.closeDocument(id);
  }

  @PostMapping("/link-documents/{targetDocumentId}")
  public void linkDocuments(@PathVariable Long targetDocumentId,
                            @RequestBody List<Long> documents) {
    if (documents.isEmpty()) {
      throw new DocMainServiceRuntimeException(DocMainServiceRuntimeException.DOCUMENT_REQUIRED);
    }

    incomingDocumentService.linkDocuments(targetDocumentId, documents);
  }

  @GetMapping("/link-documents/{targetDocumentId}")
  public List<OutgoingDocumentGetDto> getLinkedDocuments(@PathVariable Long targetDocumentId) {
    return incomingDocumentService
            .getLinkedDocuments(targetDocumentId)
            .stream()
            .map(outgoingDecoratorDocumentMapper::toDto)
            .collect(Collectors.toList());
  }

  @PutMapping("/link-documents/{targetDocumentId}")
  public void updateLinkedDocuments(@PathVariable Long targetDocumentId,
                                    @RequestBody List<OutgoingDocumentGetDto> documents) {
    if (documents.isEmpty()) {
      throw new DocMainServiceRuntimeException(DocMainServiceRuntimeException.DOCUMENT_REQUIRED);
    }

    incomingDocumentService.updateLinkedDocuments(targetDocumentId, documents);
  }

  @DeleteMapping("/link-documents/{targetDocumentId}")
  public void deleteLinkedDocuments(@PathVariable Long targetDocumentId,
                                    @RequestParam Long linkedDocumentId) {
    incomingDocumentService.deleteLinkedDocuments(targetDocumentId, linkedDocumentId);
  }
}

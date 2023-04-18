package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

  private final ProcessingDocumentService processingDocumentService;
  private final IncomingDocumentService incomingDocumentService;

  @GetMapping("/{id}")
  public IncomingDocumentDto getIncomingDocument(@PathVariable Long id) {
    return incomingDecoratorDocumentMapper
        .toDto(incomingDocumentService.getIncomingDocumentById(id));
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

  @PostMapping("/transfer-to-director")
  public void transferToDirector(@RequestBody TransferDocDto transferDocDto) {
    incomingDocumentService.transferDocumentsToDirector(transferDocDto);
  }

  @PostMapping("/transfer-to-manager")
  public void transferToManager(@RequestBody TransferDocDto transferDocDto) {
    incomingDocumentService.transferDocumentsToManager(transferDocDto);
  }

  @PutMapping("/update")
  public IncomingDocumentDto createIncomingDocument(
      @RequestBody IncomingDocumentPutDto incomingDocumentPutDto) {
    IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(
        incomingDocumentPutDto);
    return incomingDecoratorDocumentMapper.toDto(
        incomingDocumentService.updateIncomingDocument(incomingDocument));
  }
}

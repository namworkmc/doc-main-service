package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

  private final ProcessingDocumentService processingDocumentService;

  @PostMapping("/search")
  public DocPaginationDto<IncomingDocumentDto> getIncomingDocuments(
      @RequestBody(required = false) SearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {

    return paginationMapper.toDto(
        processingDocumentService
            .searchProcessingDocuments(searchCriteria, page, pageSize)
            .stream()
            .map(incomingDecoratorDocumentMapper::toDto)
            .toList(),
        processingDocumentService.getTotalElements(searchCriteria),
        processingDocumentService.getTotalPages(searchCriteria, pageSize));
  }
}

package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestPostDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.service.ReturnRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/return-requests")
public class ReturnRequestController {

  private final ReturnRequestService returnRequestService;

  @GetMapping("/{processingDocumentType}/{documentId}/list")
  public List<ReturnRequestGetDto> getReturnRequestsListByDocumentId(
      @PathVariable ProcessingDocumentTypeEnum processingDocumentType,
      @PathVariable Long documentId) {
    return returnRequestService.getReturnRequestsByDocumentId(documentId, processingDocumentType);
  }

  @GetMapping("/{processingDocumentType}/{id}/details")
  public ReturnRequestGetDto getReturnRequestDetailsById(
      @PathVariable ProcessingDocumentTypeEnum processingDocumentType,
      @PathVariable Long id) {
    return returnRequestService.getReturnRequestById(id, processingDocumentType);
  }

  @PostMapping
  public List<Long> createReturnRequest(@RequestBody ReturnRequestPostDto returnRequestPostDto) {
    return returnRequestService.createReturnRequest(returnRequestPostDto);
  }

  @PostMapping("/create-send-back-request")
  public List<Long> createSendBackRequest(@RequestBody ReturnRequestPostDto returnRequestPostDto) {
    return returnRequestService.createSendBackRequest(returnRequestPostDto);
  }
}

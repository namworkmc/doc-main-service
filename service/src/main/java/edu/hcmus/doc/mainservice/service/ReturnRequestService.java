package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestPostDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface ReturnRequestService {
  List<ReturnRequestGetDto> getReturnRequestsByDocumentId(Long documentId, ProcessingDocumentTypeEnum type);

  ReturnRequestGetDto getReturnRequestById(Long returnRequestId, ProcessingDocumentTypeEnum type);

  List<Long> createReturnRequest(ReturnRequestPostDto returnRequestDto);

  List<Long> createSendBackRequest(ReturnRequestPostDto returnRequestDto);
}

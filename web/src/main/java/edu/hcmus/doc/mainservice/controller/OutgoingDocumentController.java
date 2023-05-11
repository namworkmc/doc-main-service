package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/outgoing-documents")
public class OutgoingDocumentController extends DocAbstractController {

  private final OutgoingDocumentService outgoingDocumentService;

  @PostMapping("/create")
  public OutgoingDocumentGetDto createOutgoingDocument(@RequestBody OutgoingDocumentPostDto outgoingDocumentDto) {
    OutgoingDocument outgoingDocument = outgoingDecoratorDocumentMapper.toEntity(outgoingDocumentDto);
    return outgoingDecoratorDocumentMapper
            .toDto(outgoingDocumentService.createOutgoingDocument(outgoingDocument));
  }
}

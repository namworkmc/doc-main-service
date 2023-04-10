package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/document-types")
public class DocumentTypeController extends DocAbstractController {
    private final DocumentTypeService documentTypeService;

    @GetMapping
    public List<DocumentTypeDto> getAll() {
        return documentTypeService.findAll().stream()
                .map(documentTypeMapper::toDto)
                .toList();
    }

  @PostMapping
  public DocumentTypeDto createDocumentType(@Valid @RequestBody DocumentTypeDto documentTypeDto) {
    DocumentType documentType = documentTypeMapper.toEntity(documentTypeDto);
    return documentTypeMapper.toDto(documentTypeService.createDocumentType(documentType));
  }
}

package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

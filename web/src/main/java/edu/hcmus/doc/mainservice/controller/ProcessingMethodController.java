package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.ProcessingMethodDto;
import edu.hcmus.doc.mainservice.service.ProcessingMethodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/processing-methods")
public class ProcessingMethodController extends DocAbstractController {
    private final ProcessingMethodService processingMethodService;

    @GetMapping
    public List<ProcessingMethodDto> getAll() {
        return processingMethodService.findAll().stream()
                .map(processingMethodMapper::toDto)
                .toList();
    }
}

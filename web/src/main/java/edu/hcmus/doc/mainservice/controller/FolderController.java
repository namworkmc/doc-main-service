package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/folders")
public class FolderController extends DocAbstractController {
    private final FolderService folderService;

    @GetMapping
    public List<FolderDto> getAll() {
        return folderService.findAll().stream()
                .map(folderMapper::toDto)
                .toList();
    }
}

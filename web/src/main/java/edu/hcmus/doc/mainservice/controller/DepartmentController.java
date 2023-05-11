package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/departments")
public class DepartmentController extends DocAbstractController {
    private final DepartmentService departmentService;

    @GetMapping
    public List<DepartmentDto> getAll() {
        return departmentService.findAll().stream()
                .map(departmentMapper::toDto)
                .toList();
    }
}

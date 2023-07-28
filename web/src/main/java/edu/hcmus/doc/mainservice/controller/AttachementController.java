package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/attachments")
public class AttachementController extends DocAbstractController {
    private final AttachmentService attachmentService;

    @GetMapping("/{parentFolder}/{docId}")
    public List<String> getAttachmentNameByDocId(@PathVariable ParentFolderEnum parentFolder, @PathVariable Long docId) {
        return attachmentService.getAttachmentsByDocId(docId, parentFolder).stream().map(AttachmentDto::getFileName).toList();
    }

    @DeleteMapping("/{id}")
    public void deleteAttachmentById(@PathVariable Long id) {
        if(Objects.isNull(attachmentService.deleteAttachmentById(id))){
            throw new DocMainServiceRuntimeException("Attachment not found");
        }
    }
}

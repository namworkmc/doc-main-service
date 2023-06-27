package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import java.util.List;

public interface AttachmentService {

  void saveAttachmentsByProcessingDocumentTypeAndDocId(ParentFolderEnum parentFolder,
      AttachmentPostDto attachmentPostDto);

  List<AttachmentDto> getAttachmentsByDocId(Long docId, ParentFolderEnum parentFolder);
}

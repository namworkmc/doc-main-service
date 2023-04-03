package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import java.util.List;

public interface AttachmentService {

  List<AttachmentDto> saveAttachmentsByIncomingDocId(AttachmentPostDto attachmentPostDto);
}

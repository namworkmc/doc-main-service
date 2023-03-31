package edu.hcmus.doc.mainservice.model.dto.Attachment;

import java.util.List;
import lombok.Data;

@Data
public class AttachmentPostDto {

  private List<FileWrapper> attachments;
  private Long incomingDocId;
}

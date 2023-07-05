package edu.hcmus.doc.mainservice.model.dto.Attachment;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DocumentWithAttachmentDto {
  private Long docId;
  private List<AttachmentDto> attachments = new ArrayList<>();
}

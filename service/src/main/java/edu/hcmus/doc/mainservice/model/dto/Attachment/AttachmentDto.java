package edu.hcmus.doc.mainservice.model.dto.Attachment;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import lombok.Data;

@Data
public class AttachmentDto extends DocAbstractDto {

  private Long incomingDocId;
  private String alfrescoFileId;
  private String alfrescoFolderId;
  private String fileType;
}

package edu.hcmus.doc.mainservice.model.dto.Attachment;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.FileType;
import lombok.Data;

@Data
public class AttachmentDto extends DocAbstractDto {
  private Long docId;
  private String alfrescoFileId;
  private String alfrescoFolderId;
  private FileType fileType;
}

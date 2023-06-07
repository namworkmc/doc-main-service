package edu.hcmus.doc.mainservice.model.dto.Attachment;

import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import java.util.List;
import lombok.Data;

@Data
public class AttachmentPostDto {

  private Long docId;
  private ParentFolderEnum parentFolder;
  private List<FileWrapper> attachments;
}

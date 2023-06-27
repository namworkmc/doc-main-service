package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.Attachment;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import java.util.List;

public interface CustomAttachmentRepository {

  List<Attachment> getAttachmentsByDocId(Long docId, ParentFolderEnum parentFolder);
}

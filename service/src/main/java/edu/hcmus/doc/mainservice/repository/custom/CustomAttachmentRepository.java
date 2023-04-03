package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.Attachment;
import java.util.List;

public interface CustomAttachmentRepository {

  List<Attachment> getAttachmentsByIncomingDocId(Long incomingDocId);
}

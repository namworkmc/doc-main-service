package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OutgoingDocumentWithAttachmentPutDto {
  private String outgoingDocumentPutDto;
  private List<MultipartFile> attachments;
}

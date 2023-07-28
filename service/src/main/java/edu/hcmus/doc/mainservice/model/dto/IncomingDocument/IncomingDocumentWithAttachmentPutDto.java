package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IncomingDocumentWithAttachmentPutDto {
  private String incomingDocumentPutDto;
  private List<MultipartFile> attachments;
}

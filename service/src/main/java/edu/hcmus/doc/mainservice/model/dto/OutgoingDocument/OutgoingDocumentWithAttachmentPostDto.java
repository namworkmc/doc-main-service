package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class OutgoingDocumentWithAttachmentPostDto {
  private String outgoingDocumentPostDto;
  private List<MultipartFile> attachments;
}

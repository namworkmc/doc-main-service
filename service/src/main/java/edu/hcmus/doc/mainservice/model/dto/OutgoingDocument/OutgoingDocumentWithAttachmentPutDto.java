package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OutgoingDocumentWithAttachmentPutDto {
  @NotBlank(message = "Invalid payload")
  private String outgoingDocumentPutDto;
  private List<MultipartFile> attachments;
}

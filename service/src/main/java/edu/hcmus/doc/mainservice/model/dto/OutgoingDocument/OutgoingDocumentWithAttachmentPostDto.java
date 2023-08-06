package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OutgoingDocumentWithAttachmentPostDto {
  @NotBlank(message = "Invalid payload")
  private String outgoingDocumentPostDto;
  @NotNull(message = "Attachments are required")
  @Size(min = 1, max = 3, message = "The number of attachments must be between 1 and 3")
  private List<MultipartFile> attachments;
}

package edu.hcmus.doc.mainservice.model.dto.ReturnRequest;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ReturnRequestType;
import lombok.Data;

@Data
public class ReturnRequestGetDto {
  private Long id;
  private Long currentProcessingUserId;
  private String currentProcessingUserFullName;
  private DocSystemRoleEnum currentProcessingUserRole;
  private String currentProcessingUserRoleTitle;
  private Long previousProcessingUserId;
  private String previousProcessingUserFullName;
  private DocSystemRoleEnum previousProcessingUserRole;
  private String previousProcessingUserRoleTitle;
  private String createdAt;
  private Long documentId;
  private ProcessingDocumentTypeEnum documentType;
  private String reason;
  private ReturnRequestType returnRequestType;
}

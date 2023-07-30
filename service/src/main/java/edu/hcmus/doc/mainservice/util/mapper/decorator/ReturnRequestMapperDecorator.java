package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.util.mapper.ReturnRequestMapper;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ReturnRequestMapperDecorator implements ReturnRequestMapper {

  @Autowired
  @Qualifier("delegate")
  private ReturnRequestMapper delegate;

  @Override
  public ReturnRequestGetDto toReturnRequestGetDto(ReturnRequest entity,
      ProcessingDocumentTypeEnum type) {
    ReturnRequestGetDto dto = delegate.toReturnRequestGetDto(entity, type);
    dto.setId(entity.getId());
    dto.setCurrentProcessingUserId(entity.getCurrentProcessingUser().getId());
    dto.setCurrentProcessingUserFullName(entity.getCurrentProcessingUser().getFullName());
    dto.setCurrentProcessingUserRole(entity.getCurrentProcessingUser().getRole());
    dto.setCurrentProcessingUserRoleTitle(entity.getCurrentProcessingUser().getRoleTitle());
    dto.setPreviousProcessingUserId(entity.getPreviousProcessingUser().getId());
    dto.setPreviousProcessingUserFullName(entity.getPreviousProcessingUser().getFullName());
    dto.setPreviousProcessingUserRole(entity.getPreviousProcessingUser().getRole());
    dto.setPreviousProcessingUserRoleTitle(entity.getPreviousProcessingUser().getRoleTitle());
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    dto.setCreatedAt(entity.getCreatedDate().format(dateTimeFormatter));

    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      dto.setDocumentId(entity.getIncomingDocument().getId());
    } else {
      dto.setDocumentId(entity.getOutgoingDocument().getId());
    }

    dto.setReason(entity.getReason());
    dto.setReturnRequestType(entity.getType());
    return dto;
  }
}

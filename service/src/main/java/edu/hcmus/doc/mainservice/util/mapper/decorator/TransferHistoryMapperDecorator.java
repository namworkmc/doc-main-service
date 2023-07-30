package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.util.mapper.TransferHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TransferHistoryMapperDecorator implements TransferHistoryMapper {

  @Autowired
  @Qualifier("delegate")
  private TransferHistoryMapper delegate;

  @Autowired
  AttachmentService attachmentService;

  @Override
  public TransferHistoryDto toDto(TransferHistory entity) {

    TransferHistoryDto dto = new TransferHistoryDto();
    if (entity.getIncomingDocumentIds() != null && !entity.getIncomingDocumentIds().isEmpty()) {
      dto.setDocumentIds(entity.getIncomingDocumentIds());

      dto.setAttachments(attachmentService.getDocumentsWithAttachmentsByDocIds(
          entity.getIncomingDocumentIds(), ParentFolderEnum.ICD));
    }

    if (entity.getOutgoingDocumentIds() != null && !entity.getOutgoingDocumentIds().isEmpty()) {
      dto.setDocumentIds(entity.getOutgoingDocumentIds());

      dto.setAttachments(attachmentService.getDocumentsWithAttachmentsByDocIds(
          entity.getOutgoingDocumentIds(), ParentFolderEnum.OGD));
    }
    dto.setId(entity.getId());
    dto.setCreatedDate(entity.getCreatedDate().toLocalDate());
    dto.setCreatedTime(entity.getCreatedDate().toLocalTime());
    dto.setProcessingDuration(entity.getProcessingDuration());
    dto.setIsInfiniteProcessingTime(entity.getIsInfiniteProcessingTime());
    dto.setIsTransferToSameLevel(entity.getIsTransferToSameLevel());
    if (entity.getProcessingMethod() != null) {
      dto.setProcessingMethod(entity.getProcessingMethod().getName());
    } else {
      dto.setProcessingMethod(null);
    }
    dto.setSenderId(entity.getSender().getId());
    dto.setReceiverId(entity.getReceiver().getId());
    dto.setSenderName(entity.getSender().getFullName());
    dto.setReceiverName(entity.getReceiver().getFullName());

    if (entity.getReturnRequest() != null) {
      dto.setReturnRequestId(entity.getReturnRequest().getId());
    } else {
      dto.setReturnRequestId(null);
    }
    return dto;
  }
}

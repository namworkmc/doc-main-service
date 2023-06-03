package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.util.mapper.TransferHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TransferHistoryMapperDecorator implements TransferHistoryMapper {

  @Autowired
  @Qualifier("delegate")
  private TransferHistoryMapper delegate;

  @Override
  public TransferHistoryDto toDto(TransferHistory entity) {

    TransferHistoryDto dto = new TransferHistoryDto();
    if (entity.getIncomingDocumentIds() != null && !entity.getIncomingDocumentIds().isEmpty()) {
      dto.setDocumentIds(entity.getIncomingDocumentIds());
    }

    if (entity.getOutgoingDocumentIds() != null && !entity.getOutgoingDocumentIds().isEmpty()) {
      dto.setDocumentIds(entity.getOutgoingDocumentIds());
    }
    dto.setId(entity.getId());
    dto.setCreatedDate(entity.getCreatedDate());
    dto.setProcessingDuration(entity.getProcessingDuration());
    dto.setIsInfiniteProcessingTime(entity.getIsInfiniteProcessingTime());
    dto.setIsTransferToSameLevel(entity.getIsTransferToSameLevel());
    dto.setProcessMethod(entity.getProcessMethod());
    dto.setSenderId(entity.getSender().getId());
    dto.setReceiverId(entity.getReceiver().getId());
    dto.setSenderName(entity.getSender().getFullName());
    dto.setReceiverName(entity.getReceiver().getFullName());

    return dto;
  }
}

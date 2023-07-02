package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.util.mapper.decorator.TransferHistoryMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
@DecoratedWith(TransferHistoryMapperDecorator.class)
public interface TransferHistoryMapper {

  @Mapping(target = "documentIds", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "processingDuration", ignore = true)
  @Mapping(target = "isInfiniteProcessingTime", ignore = true)
  @Mapping(target = "processingMethod", ignore = true)
  @Mapping(target = "senderId", ignore = true)
  @Mapping(target = "receiverId", ignore = true)
  @Mapping(target = "senderName", ignore = true)
  @Mapping(target = "receiverName", ignore = true)
  @Mapping(target = "isTransferToSameLevel", ignore = true)
  TransferHistoryDto toDto(TransferHistory entity);

}

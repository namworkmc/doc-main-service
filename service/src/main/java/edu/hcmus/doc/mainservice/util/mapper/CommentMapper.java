package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.CommentDto;
import edu.hcmus.doc.mainservice.model.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING, uses = {IncomingDocumentMapper.class})
public interface CommentMapper {

  @Mapping(target = "outgoingDocument", ignore = true)
  @Mapping(target = "incomingDocument", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "updatedDate", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  Comment toEntity(CommentDto commentDto);

  CommentDto toDto(Comment comment);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}

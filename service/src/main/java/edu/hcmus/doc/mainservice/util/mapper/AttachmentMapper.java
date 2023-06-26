package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.model.entity.Attachment;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
@DecoratedWith(AttachmentMapperDecorator.class)
public interface AttachmentMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "alfrescoFileId", target = "alfrescoFileId")
  @Mapping(source = "alfrescoFolderId", target = "alfrescoFolderId")
  @Mapping(source = "fileType", target = "fileType")
  @Mapping(target = "fileName", ignore = true)
  AttachmentDto toDto(Attachment attachment);

  @Mapping(source = "attachmentDto.alfrescoFileId", target = "alfrescoFileId")
  @Mapping(source = "attachmentDto.alfrescoFolderId", target = "alfrescoFolderId")
  @Mapping(source = "attachmentDto.fileType", target = "fileType")
  Attachment toEntity(AttachmentDto attachmentDto);

  AttachmentPostDto toAttachmentPostDto(Long incomingDocId, List<MultipartFile> attachments);


  AttachmentDto convertFileDtoToAttachmentDto(Long incomingDocId, FileDto fileDto);

}

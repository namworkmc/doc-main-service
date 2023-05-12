package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.FileWrapper;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.model.entity.Attachment;
import edu.hcmus.doc.mainservice.model.enums.FileType;
import edu.hcmus.doc.mainservice.util.mapper.AttachmentMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

public abstract class AttachmentMapperDecorator implements AttachmentMapper {

  @Autowired
  @Qualifier("delegate")
  private AttachmentMapper delegate;


  @Override
  public AttachmentPostDto toAttachmentPostDto(Long docId,
      List<MultipartFile> attachments) {
    AttachmentPostDto attachmentPostDto = new AttachmentPostDto();
    attachmentPostDto.setDocId(docId);
    List<FileWrapper> fileWrappers = attachments.stream()
        .map(file -> {
          FileWrapper fileWrapper = new FileWrapper();
          try {
            fileWrapper.setData(file.getBytes());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          fileWrapper.setFileName(file.getOriginalFilename());
          fileWrapper.setContentType(file.getContentType());
          return fileWrapper;
        }).collect(Collectors.toList());
    attachmentPostDto.setAttachments(fileWrappers);
    return attachmentPostDto;
  }

  @Override
  public Attachment toEntity(AttachmentDto attachmentDto) {
    Attachment entity = delegate.toEntity(attachmentDto);

    return entity;
  }

  @Override
  public AttachmentDto convertFileDtoToAttachmentDto(Long docId, FileDto fileDto) {
    AttachmentDto attachmentDto = new AttachmentDto();
    attachmentDto.setAlfrescoFileId(fileDto.getId());
    attachmentDto.setAlfrescoFolderId(fileDto.getParentFolderId());
    attachmentDto.setFileType(FileType.fromValue(fileDto.getMimeType()));
    attachmentDto.setDocId(docId);
    return attachmentDto;
  }

}

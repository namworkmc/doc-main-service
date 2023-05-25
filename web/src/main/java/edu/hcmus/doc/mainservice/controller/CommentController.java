package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.CommentDto;
import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.service.CommentService;
import edu.hcmus.doc.mainservice.util.mapper.CommentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/comments")
public class CommentController {

  private final CommentMapper commentMapper;

  private final CommentService commentService;

  @GetMapping("/{processingDocumentType}/{documentId}")
  public List<CommentDto> getCommentByIncomingDocumentId(
      @PathVariable ProcessingDocumentTypeEnum processingDocumentType,
      @PathVariable Long documentId) {
    return commentService.getCommentByTypeAndDocumentId(processingDocumentType, documentId)
        .stream()
        .map(commentMapper::toDto)
        .toList();
  }

  @PostMapping("/{documentId}")
  public Long createComment(
      @PathVariable Long documentId,
      @RequestBody CommentDto commentDto) {
    Comment comment = commentMapper.toEntity(commentDto);
    return commentService.createComment(documentId, commentDto.getProcessingDocumentType(), comment);
  }
}

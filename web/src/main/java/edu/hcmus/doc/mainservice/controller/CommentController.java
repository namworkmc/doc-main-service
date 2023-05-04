package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.CommentDto;
import edu.hcmus.doc.mainservice.model.entity.Comment;
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

  @GetMapping("/incoming-documents/{incomingDocumentId}")
  public List<CommentDto> getCommentByIncomingDocumentId(@PathVariable Long incomingDocumentId) {
    return commentService.getCommentByIncomingDocumentId(incomingDocumentId)
        .stream()
        .map(commentMapper::toDto)
        .toList();
  }

  @PostMapping("/incoming-documents/{incomingDocumentId}")
  public Long createComment(@PathVariable Long incomingDocumentId, @RequestBody CommentDto commentDto) {
    Comment comment = commentMapper.toEntity(commentDto);
    return commentService.createComment(incomingDocumentId, comment);
  }
}
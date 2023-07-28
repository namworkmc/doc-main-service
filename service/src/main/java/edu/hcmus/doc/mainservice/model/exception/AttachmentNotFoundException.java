package edu.hcmus.doc.mainservice.model.exception;

import lombok.Getter;

@Getter
public class AttachmentNotFoundException extends DocNotFoundException {

  public static final String ATTACHMENT_NOT_FOUND = "ATTACHMENT.NOT_FOUND";
  public AttachmentNotFoundException(String message) {
    super(message);
  }
}

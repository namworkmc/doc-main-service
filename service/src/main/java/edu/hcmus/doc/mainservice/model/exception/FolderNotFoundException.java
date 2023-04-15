package edu.hcmus.doc.mainservice.model.exception;

import lombok.Getter;

@Getter
public class FolderNotFoundException extends DocNotFoundException {

  public static final String FOLDER_NOT_FOUND = "FOLDER.NOT_FOUND";

  public FolderNotFoundException(String message) {
    super(message);
  }
}

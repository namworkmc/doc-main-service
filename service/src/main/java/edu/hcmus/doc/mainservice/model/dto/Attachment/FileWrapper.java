package edu.hcmus.doc.mainservice.model.dto.Attachment;

import lombok.Data;

@Data
public class FileWrapper {

 private byte[] data;
 private String fileName;
 private String contentType;
}

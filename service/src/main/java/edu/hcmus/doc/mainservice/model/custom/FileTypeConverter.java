package edu.hcmus.doc.mainservice.model.custom;

import edu.hcmus.doc.mainservice.model.enums.FileType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FileTypeConverter implements AttributeConverter<FileType, String> {

  @Override
  public String convertToDatabaseColumn(FileType fileType) {
    if (fileType == null) {
      return null;
    }
    return fileType.value;
  }

  @Override
  public FileType convertToEntityAttribute(String value) {
    if (value == null) {
      return null;
    }
    return FileType.fromValue(value);
  }
}
package edu.hcmus.doc.mainservice.model.custom;

import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProcessMethodConverter implements AttributeConverter<ProcessMethod, String> {
  @Override
  public String convertToDatabaseColumn(ProcessMethod processMethod) {
    if (processMethod == null) {
      return null;
    }
    return processMethod.value;
  }

  @Override
  public ProcessMethod convertToEntityAttribute(String value) {
    if (value == null) {
      return null;
    }
    return ProcessMethod.fromValue(value);
  }
}

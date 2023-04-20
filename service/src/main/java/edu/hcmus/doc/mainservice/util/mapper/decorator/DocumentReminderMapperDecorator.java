package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.util.mapper.DocumentReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class DocumentReminderMapperDecorator implements DocumentReminderMapper {

  @Autowired
  @Qualifier("delegate")
  private DocumentReminderMapper delegate;

  @Override
  public DocumentReminderDetailsDto toDto(DocumentReminder documentReminder) {
    return delegate.toDto(documentReminder);
  }
}

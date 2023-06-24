package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderWrapperDto;
import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.util.mapper.DocumentReminderMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

  @Override
  public DocumentReminderDetailsDto toDto(ProcessingDocument processingDocument, LocalDate date) {
    return delegate.toDto(processingDocument, date);
  }

  @Override
  public DocumentReminderWrapperDto toDto(
      Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> processingDocumentReminderMap,
      LocalDate date) {
    DocumentReminderWrapperDto dto = delegate.toDto(processingDocumentReminderMap, date);
    dto.setActive(
        Optional.ofNullable(processingDocumentReminderMap.get(DocumentReminderStatusEnum.ACTIVE))
            .orElse(Collections.emptySet())
            .stream()
            .map(p -> toDto(p, date))
            .toList());
    dto.setCloseToExpiration(
        Optional.ofNullable(
                processingDocumentReminderMap.get(DocumentReminderStatusEnum.CLOSE_TO_EXPIRATION))
            .orElse(Collections.emptySet())
            .stream()
            .map(p -> toDto(p, date))
            .toList()
    );
    dto.setExpired(
        Optional.ofNullable(processingDocumentReminderMap.get(DocumentReminderStatusEnum.EXPIRED))
            .orElse(Collections.emptySet())
            .stream()
            .map(p -> toDto(p, date))
            .toList());

    return dto;
  }
}

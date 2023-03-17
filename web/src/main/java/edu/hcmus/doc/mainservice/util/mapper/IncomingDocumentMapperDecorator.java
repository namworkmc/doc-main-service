package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class IncomingDocumentMapperDecorator implements IncomingDocumentMapper {

  @Autowired
  @Qualifier("delegate")
  private IncomingDocumentMapper delegate;

  @Override
  public IncomingDocumentDto toDto(IncomingDocument incomingDocument) {
    return delegate.toDto(incomingDocument);
  }

  @Override
  public IncomingDocumentDto toDto(ProcessingDocument processingDocument) {
    IncomingDocumentDto dto = delegate.toDto(processingDocument.getIncomingDoc());
    dto.setStatus(processingDocument.getStatus());
    dto.setProcessingDuration(processingDocument.getProcessingDuration());
    return dto;
  }
}

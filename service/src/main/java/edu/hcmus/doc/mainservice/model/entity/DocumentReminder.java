package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "document_reminder", schema = "doc_main", catalog = "doc")
public class DocumentReminder extends DocAbstractIdEntity {

  @OneToOne
  @JoinColumn(name = "processing_doc_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocument processingDoc;

  @Column(name = "execution_time", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
  private LocalDateTime executionTime;

  @Column(name = "expiration_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
  private LocalDate expirationDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private DocumentReminderStatusEnum status;

  @Column(name = "is_opened", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
  private boolean isOpened;
}

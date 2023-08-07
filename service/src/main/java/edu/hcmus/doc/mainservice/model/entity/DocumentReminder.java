package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "document_reminder", schema = SCHEMA_NAME, catalog = CATALOG_NAME)
public class DocumentReminder extends DocAbstractIdEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "processing_user_id", referencedColumnName = "id", nullable = false)
  private ProcessingUser processingUser;

  @NotNull
  @Column(name = "expiration_date", nullable = false)
  private LocalDate expirationDate;

  @NotNull
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private DocumentReminderStatusEnum status;

  @Column(name = "is_opened", nullable = false)
  private boolean isOpened;
}

package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.RequestStatus;
import java.time.LocalDate;
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
@Table(name = "extension_request", schema = "doc_main", catalog = "doc")
public class ExtensionRequest extends DocAbstractIdEntity {

  @OneToOne
  @JoinColumn(name = "processing_doc_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocument processingDoc = new ProcessingDocument();

  @Column(name = "reason", nullable = false, length = 200)
  private String reason;

  @Column(name = "extended_until", nullable = false)
  private LocalDate extendedUntil;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private RequestStatus status;
}

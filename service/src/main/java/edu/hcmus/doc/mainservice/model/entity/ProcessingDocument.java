package edu.hcmus.doc.mainservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_document", schema = "doc_main", catalog = "doc")
public class ProcessingDocument extends DocAbstractIdEntity {

  @OneToOne
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", updatable = false, nullable = false)
  private IncomingDocument incomingDoc;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProcessingStatus status;

  @Column(name = "is_opened", nullable = false, columnDefinition = "BOOL DEFAULT FALSE")
  private boolean isOpened;

  @Column(name = "processing_request", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String processingRequest;

  @Transient
  private LocalDate processingDuration;

  @OneToOne
  @JoinColumn(name = "outgoing_document_id", referencedColumnName = "id")
  private OutgoingDocument outgoingDocument;

  public void initIncomingDocument() {
    if (this.incomingDoc == null) {
      this.incomingDoc = new IncomingDocument();
    }
  }

  @JsonIgnore
  public boolean isIncomingDocument() {
    return this.incomingDoc != null;
  }

  @JsonIgnore
  public boolean isOutgoingDocument() {
    return !isIncomingDocument();
  }
}

package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transfer_history", schema = SCHEMA_NAME, catalog = CATALOG_NAME)
public class TransferHistory extends DocAbstractIdEntity {

  @ManyToOne
  @JoinColumn(name = "sender_id", columnDefinition = "BIGINT", nullable = false)
  private User sender;

  @ManyToOne
  @JoinColumn(name = "receiver_id", columnDefinition = "BIGINT", nullable = false)
  private User receiver;

  @ElementCollection
  @CollectionTable(name = "transfer_history_incoming_document", joinColumns = @JoinColumn(name = "transfer_history_id"), catalog = CATALOG_NAME, schema = SCHEMA_NAME)
  @Column(name = "incoming_doc_id", columnDefinition = "BIGINT")
  private List<Long> incomingDocumentIds;

  @ElementCollection
  @CollectionTable(name = "transfer_history_outgoing_document", joinColumns = @JoinColumn(name = "transfer_history_id"), catalog = CATALOG_NAME, schema = SCHEMA_NAME)
  @Column(name = "outgoing_doc_id", columnDefinition = "BIGINT")
  private List<Long> outgoingDocumentIds;

  @OneToOne
  @JoinColumn(name = "processing_method_id", referencedColumnName = "id")
  private ProcessingMethod processingMethod;

  @Column(name = "processing_duration", columnDefinition = "TIMESTAMP")
  private LocalDate processingDuration;

  @Column(name = "is_infinite_processing_time", columnDefinition = "BOOLEAN")
  private Boolean isInfiniteProcessingTime;

  @Column(name = "is_transfer_to_same_level", columnDefinition = "BOOLEAN")
  private Boolean isTransferToSameLevel;

  @OneToOne
  @JoinColumn(name = "return_request_id", referencedColumnName = "id")
  private ReturnRequest returnRequest;

  @Column(name = "is_read", nullable = false, columnDefinition = "BOOL NOT NULL DEFAULT FALSE")
  private Boolean isRead;
}

package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import edu.hcmus.doc.mainservice.model.custom.ProcessMethodConverter;
import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @Column(name = "process_method", nullable = false)
  @Convert(converter = ProcessMethodConverter.class)
  private ProcessMethod processMethod;

  @Column(name = "processing_duration", columnDefinition = "TIMESTAMP")
  private LocalDate processingDuration;

  @Column(name = "is_infinite_processing_time", columnDefinition = "BOOLEAN")
  private Boolean isInfiniteProcessingTime;

  @Column(name = "is_transfer_to_same_level", columnDefinition = "BOOLEAN")
  private Boolean isTransferToSameLevel;
}

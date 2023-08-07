package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.model.enums.ReturnRequestType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "return_request", schema = "doc_main", catalog = "doc")
public class ReturnRequest extends DocAbstractIdEntity {

  @Column(name = "reason", nullable = false, length = 200, columnDefinition = "VARCHAR(200) NOT NULL")
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ExtendRequestStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "return_type", nullable = false)
  private ReturnRequestType type;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id")
  private IncomingDocument incomingDocument;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "outgoing_doc_id", referencedColumnName = "id")
  private OutgoingDocument outgoingDocument;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "current_processing_user_id", referencedColumnName = "id", updatable = false, nullable = false)
  private User currentProcessingUser;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "previous_processing_user_id", referencedColumnName = "id", updatable = false, nullable = false)
  private User previousProcessingUser;
}

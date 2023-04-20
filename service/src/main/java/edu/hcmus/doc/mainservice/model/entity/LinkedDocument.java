package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "linked_document", schema = "doc_main", catalog = "doc")
public class LinkedDocument extends DocAbstractIdEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDocument = new IncomingDocument();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "outgoing_doc_id", referencedColumnName = "id", nullable = false)
  private OutgoingDocument outgoingDocument = new OutgoingDocument();
}

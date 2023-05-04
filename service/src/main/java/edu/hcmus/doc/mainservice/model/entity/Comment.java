package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comment", schema = "doc_main", catalog = "doc")
public class Comment extends DocAbstractIdEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_document_id", referencedColumnName = "id")
  private IncomingDocument incomingDocument;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "outgoing_document_id", referencedColumnName = "id")
  private OutgoingDocument outgoingDocument;
}

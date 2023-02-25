package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.entity.pk.LinkedDocumentPK;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "linked_document", schema = "doc_main", catalog = "doc")
public class LinkedDocument extends DocBaseEntity {

  @EmbeddedId
  private LinkedDocumentPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("incomingDocId")
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDocument;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("outgoingDocId")
  @JoinColumn(name = "outgoing_doc_id", referencedColumnName = "id", nullable = false)
  private OutgoingDocument outgoingDocument;

  public LinkedDocument(IncomingDocument incomingDocument, OutgoingDocument outgoingDocument) {
    this.id = new LinkedDocumentPK(incomingDocument.getId(), outgoingDocument.getId());
    this.incomingDocument = incomingDocument;
    this.outgoingDocument = outgoingDocument;
  }
}

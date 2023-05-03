package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processed_document", schema = "doc_main", catalog = "doc")
public class ProcessedDocument extends DocAbstractIdEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDoc;
}

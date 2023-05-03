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
@Table(name = "feedback", schema = "doc_main", catalog = "doc")
public class Feedback extends DocAbstractIdEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_doc_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocument processingDocument;

  @Column(name = "content", nullable = false, length = 200)
  private String content;
}

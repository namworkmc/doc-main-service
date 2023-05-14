package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "document_type", schema = "doc_main", catalog = "doc")
public class DocumentType extends DocAbstractIdEntity {

  @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String type;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;
}

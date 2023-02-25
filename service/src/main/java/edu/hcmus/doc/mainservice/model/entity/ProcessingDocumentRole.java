package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_document_role", schema = "doc_main", catalog = "doc")
public class ProcessingDocumentRole extends DocAbstractEntity {

  @Column(name = "name", nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL")
  private String name;
}

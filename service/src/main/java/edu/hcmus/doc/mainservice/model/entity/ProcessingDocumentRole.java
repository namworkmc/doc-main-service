package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_document_role", schema = "doc_main", catalog = "doc")
public class ProcessingDocumentRole extends DocAbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL")
  private ProcessingDocumentRoleEnum name;
}

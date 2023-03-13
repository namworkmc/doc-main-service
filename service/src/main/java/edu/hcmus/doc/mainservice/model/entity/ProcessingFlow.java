package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "processing_flow", schema = "doc_main", catalog = "doc")
public class ProcessingFlow extends DocAbstractEntity {

  @Column(name = "flow_version", nullable = false, columnDefinition = "BIGINT NOT NULL CHECK (flow_version > 0)")
  private Long flowVersion;

  @Column(name = "doc_type_id", nullable = false, columnDefinition = "BIGINT NOT NULL")
  private Long docTypeId;

  @Type(type = "edu.hcmus.doc.mainservice.model.custom.CustomArrayType")
  @Column(name = "flow", nullable = false)
  private String[] flow;
}

package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.entity.pk.ProcessingFlowPK;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "processing_flow", schema = "doc_main", catalog = "doc")
public class ProcessingFlow extends DocBaseEntity {

  @EmbeddedId
  private ProcessingFlowPK id;

  @Type(type = "edu.hcmus.doc.mainservice.model.custom.CustomArrayType")
  @Column(name = "flow", nullable = false)
  private String[] flow;
}

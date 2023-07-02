package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.DocConst;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "processing_method", schema = DocConst.SCHEMA_NAME, catalog = DocConst.CATALOG_NAME)
public class ProcessingMethod extends DocAbstractIdEntity {

  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private String name;
}

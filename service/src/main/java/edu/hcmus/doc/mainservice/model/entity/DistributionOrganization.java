package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "distribution_organization", schema = "doc_main", catalog = "doc")
public class DistributionOrganization extends DocAbstractEntity {

  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String name;

  @Column(name = "symbol", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String symbol;
}

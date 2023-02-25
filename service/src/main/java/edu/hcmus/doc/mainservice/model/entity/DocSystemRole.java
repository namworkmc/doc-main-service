package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "doc_system_role", schema = "doc_main", catalog = "doc")
public class DocSystemRole extends DocAbstractEntity {

  @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(20) NOT NULL")
  private String name;
}

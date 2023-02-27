package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "doc_system_role", schema = "doc_main", catalog = "doc")
public class DocSystemRole extends DocAbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(20) NOT NULL")
  private DocSystemRoleEnum name;
}

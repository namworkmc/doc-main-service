package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "department", schema = "doc_main", catalog = "doc")
public class Department extends DocAbstractIdEntity {

  @Column(name = "department_name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private String departmentName;
}

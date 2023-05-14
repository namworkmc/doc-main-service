package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.DocConst;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "department", schema = DocConst.SCHEMA_NAME, catalog = DocConst.CATALOG_NAME)
public class Department extends DocAbstractIdEntity {

  @Column(name = "department_name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private String departmentName;

  @OneToOne
  @JoinColumn(name = "truong_phong_id", columnDefinition = "BIGINT")
  private User truongPhong;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;
}

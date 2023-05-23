package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "return_request", schema = "doc_main", catalog = "doc")
public class ReturnRequest extends DocAbstractIdEntity {

  @Column(name = "reason", nullable = false, length = 200, columnDefinition = "VARCHAR(200) NOT NULL")
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ExtendRequestStatus status;
}

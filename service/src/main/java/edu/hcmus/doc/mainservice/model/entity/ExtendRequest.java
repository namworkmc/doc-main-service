package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "extend_request", schema = SCHEMA_NAME, catalog = CATALOG_NAME)
public class ExtendRequest extends DocAbstractIdEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_user_id", referencedColumnName = "id", nullable = false)
  private ProcessingUser processingUser;

  @Column(name = "old_deadline", nullable = false)
  private LocalDate oldDeadline;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ExtendRequestStatus status;

  @Column(name = "reason", nullable = false, length = 200)
  private String reason;

  @Column(name = "new_deadline", nullable = false)
  private LocalDate newDeadline;

  @JoinColumn(name = "validated_by", referencedColumnName = "id")
  @OneToOne(fetch = FetchType.LAZY)
  private User validatedBy;
}

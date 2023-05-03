package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
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
@Table(name = "extension_request", schema = "doc_main", catalog = "doc")
public class ExtensionRequest extends DocAbstractIdEntity {

  @OneToOne
  @JoinColumn(name = "processing_user_id", referencedColumnName = "id", nullable = false)
  private ProcessingUser processingUser;

  @Column(name = "reason", nullable = false, length = 200)
  private String reason;

  @Column(name = "extended_until", nullable = false)
  private LocalDate extendedUntil;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ExtensionRequestStatus status;

  @JoinColumn(name = "validated_by", referencedColumnName = "id")
  @OneToOne(fetch = FetchType.LAZY)
  private User validatedBy;

  @Column(name = "old_expired_date", nullable = false)
  private LocalDate oldExpiredDate;
}

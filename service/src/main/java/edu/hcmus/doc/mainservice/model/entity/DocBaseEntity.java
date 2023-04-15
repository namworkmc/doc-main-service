package edu.hcmus.doc.mainservice.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DocBaseEntity {

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;

  @CreationTimestamp
  @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime createdDate;

  @CreatedBy
  protected String createdBy;

  @UpdateTimestamp
  @Column(name = "updated_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime updatedDate;

  @LastModifiedBy
  protected String updatedBy;
}

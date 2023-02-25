package edu.hcmus.doc.mainservice.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class DocBaseEntity {

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;

  @CreationTimestamp
  @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime createdDate;

  @CreatedBy
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "created_by",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false,
      columnDefinition = "BIGINT"
  )
  protected User createdBy;

  @UpdateTimestamp
  @Column(name = "updated_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime updatedDate;

  @LastModifiedBy
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", referencedColumnName = "id", columnDefinition = "BIGINT")
  protected User updatedBy;
}

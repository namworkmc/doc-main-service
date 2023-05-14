package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.model.entity.DocAbstractVersionEntity.IS_DELETED_PARAM;

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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
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
@FilterDef(
    name = DocAbstractVersionEntity.DELETED_FILTER,
    parameters = @ParamDef(name = IS_DELETED_PARAM, type = "boolean")
)
@Filter(name = DocAbstractVersionEntity.DELETED_FILTER, condition = "is_deleted = :isDeleted")
public abstract class DocAbstractVersionEntity {

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;

  @CreationTimestamp
  @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime createdDate;

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "VARCHAR(50) NOT NULL DEFAULT 'SYSTEM'")
  protected String createdBy;

  @UpdateTimestamp
  @Column(name = "updated_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime updatedDate;

  @LastModifiedBy
  @Column(name = "updated_by", nullable = false, columnDefinition = "VARCHAR(50) NOT NULL DEFAULT 'SYSTEM'")
  protected String updatedBy;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOL NOT NULL DEFAULT FALSE")
  protected boolean isDeleted;

  public static final String DELETED_FILTER = "deletedFilter";

  public static final String IS_DELETED_PARAM = "isDeleted";
}

package edu.hcmus.doc.mainservice.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "password_expiration", schema = "doc_main", catalog = "doc")
public class PasswordExpiration extends DocAbstractIdEntity {

  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @Column(name = "creation_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  private LocalDateTime creationTime;

  @Column(name = "needs_change", nullable = false, columnDefinition = "BOOL NOT NULL DEFAULT FALSE")
  private boolean needsChange;
}

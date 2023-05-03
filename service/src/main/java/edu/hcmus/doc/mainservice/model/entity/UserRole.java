package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_role", schema = "doc_main", catalog = "doc")
public class UserRole extends DocAbstractIdEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "role_name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private DocSystemRoleEnum role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;
}

package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "user", schema = "doc_main", catalog = "doc")
public class User extends DocAbstractIdEntity {

  @Column(name = "full_name", columnDefinition = "VARCHAR(255)")
  private String fullName;

  @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String username;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String password;

  @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String email;

  @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  @Enumerated(EnumType.STRING)
  private DocSystemRoleEnum role;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", referencedColumnName = "id", columnDefinition = "BIGINT")
  private Department department;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

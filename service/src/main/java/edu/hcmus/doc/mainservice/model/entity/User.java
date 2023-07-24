package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "user", schema = SCHEMA_NAME, catalog = CATALOG_NAME)
public class User extends DocAbstractIdEntity {

  @Column(name = "full_name")
  private String fullName;

  @NotNull
  @Column(name = "username", updatable = false, nullable = false, unique = true)
  private String username;

  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @NotNull
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private DocSystemRoleEnum role;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", referencedColumnName = "id")
  private Department department;

  @Column(name = "role_title")
  private String roleTitle;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private Set<DocFirebaseTokenEntity> firebaseTokens = new HashSet<>();

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

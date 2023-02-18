package edu.hcmus.doc.mainservice.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User extends DocAbstractEntity {

  @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(255)")
  private String firstName;

  @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(255)")
  private String lastName;

  @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String username;

  @Column(name = "password", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String password;

  @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String email;

  @Exclude
  @OneToMany(mappedBy = "user")
  private Set<UserRole> roles = new HashSet<>();

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

package edu.hcmus.doc.model.entity;

import edu.hcmus.doc.model.entity.pk.UserRolePK;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_role", schema = "public")
@IdClass(UserRolePK.class)
public class UserRole {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @Id
  @Column(name = "role_id")
  private Long roleId;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @MapsId("roleId")
  private DocRole role;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    UserRole userRole = (UserRole) o;
    return userId != null && Objects.equals(userId, userRole.userId)
        && roleId != null && Objects.equals(roleId, userRole.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, roleId);
  }
}

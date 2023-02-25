package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.entity.pk.UserRolePK;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_role", schema = "doc_main", catalog = "doc")
public class UserRole extends DocBaseEntity {

  @EmbeddedId
  private UserRolePK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("roleId")
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  private DocSystemRole role;

  public UserRole(User user, DocSystemRole role) {
    this.id = new UserRolePK(user.getId(), role.getId());
    this.user = user;
    this.role = role;
  }
}

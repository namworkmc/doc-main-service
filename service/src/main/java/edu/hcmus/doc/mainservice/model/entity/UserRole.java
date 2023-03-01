package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.entity.pk.UserRolePK;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
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
  private UserRolePK id = new UserRolePK();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  public UserRole(User user, DocSystemRoleEnum role) {
    this.user = user;
    this.setRole(role);
  }

  public DocSystemRoleEnum getRole() {
    return id.getRole();
  }

  public void setRole(DocSystemRoleEnum role) {
    id.setRole(role);
  }
}

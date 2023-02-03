package edu.hcmus.doc.model.entity;

import edu.hcmus.doc.model.enums.DocRoleEnum;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "doc_role", schema = "public")
public class DocRole extends DocAbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(10)")
  private DocRoleEnum name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DocRole docRole = (DocRole) o;
    return getId() != null && Objects.equals(getId(), docRole.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

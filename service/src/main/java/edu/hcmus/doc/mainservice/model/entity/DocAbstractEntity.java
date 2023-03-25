package edu.hcmus.doc.mainservice.model.entity;

import java.util.Objects;
import javax.persistence.*;

import lombok.Data;
import org.hibernate.Hibernate;

@Data
@MappedSuperclass
public abstract class DocAbstractEntity extends DocBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, columnDefinition = "SERIAL")
  protected Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DocAbstractEntity that = (DocAbstractEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

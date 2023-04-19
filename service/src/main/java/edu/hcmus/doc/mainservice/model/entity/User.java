package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user", schema = "doc_main", catalog = "doc")
public class User extends DocAbstractEntity {

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
}

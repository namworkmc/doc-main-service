package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user", schema = "doc_main", catalog = "doc")
public class User extends DocAbstractEntity {

  @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
  private String firstName;

  @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
  private String lastName;

  @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String username;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String password;

  @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String email;
}

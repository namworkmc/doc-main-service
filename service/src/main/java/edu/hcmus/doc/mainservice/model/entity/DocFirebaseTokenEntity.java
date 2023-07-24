package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.DocConst;
import edu.hcmus.doc.mainservice.model.enums.DocFirebaseTokenType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "firebase_token", schema = DocConst.SCHEMA_NAME, catalog = DocConst.CATALOG_NAME)
public class DocFirebaseTokenEntity extends DocAbstractIdEntity {

  @NotNull
  @NotBlank
  @Column(name = "token")
  private String token;

  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private DocFirebaseTokenType type;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
}

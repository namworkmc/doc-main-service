package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sending_level", schema = "doc_main", catalog = "doc")
public class SendingLevel extends DocAbstractIdEntity {

  @Column(name = "level", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String level;
}

package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "folder", schema = "doc_main", catalog = "doc")
public class Folder extends DocAbstractIdEntity {

  @Column(name = "folder_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  @Getter(AccessLevel.NONE)
  private String folderName;

  @Column(name="next_number", nullable = false)
  private Long nextNumber;

  @Column(name="year", nullable = false)
  private int year;

  public String getFolderName() {
    return folderName + " " + year;
  }
}

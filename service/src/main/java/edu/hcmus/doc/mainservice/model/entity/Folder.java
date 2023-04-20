package edu.hcmus.doc.mainservice.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "folder", schema = "doc_main", catalog = "doc")
public class Folder extends DocAbstractIdEntity {

  @Column(name = "folder_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String folderName;

  @Column(name="next_number", nullable = false)
  private Long nextNumber;

  @Column(name="year", nullable = false)
  private int year;
}

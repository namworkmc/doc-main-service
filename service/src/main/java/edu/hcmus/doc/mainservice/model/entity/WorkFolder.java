package edu.hcmus.doc.mainservice.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "work_folder", schema = "doc_main", catalog = "doc")
public class WorkFolder extends DocAbstractIdEntity {
  @Column(name = "work_folder_number", nullable = false)
  private Long workFolderNumber;

  @Column(name = "work_folder_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String workFolderName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;
}

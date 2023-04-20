package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "processing_user_role", schema = "doc_main", catalog = "doc")
public class ProcessingUserRole extends DocAbstractIdEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "role_name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private ProcessingDocumentRoleEnum role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_user_id", referencedColumnName = "id", nullable = false)
  private ProcessingUser processingUser = new ProcessingUser();
}

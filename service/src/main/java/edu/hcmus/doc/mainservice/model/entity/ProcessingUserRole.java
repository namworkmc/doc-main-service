package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.entity.pk.ProcessingUserRolePK;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_user_role", schema = "doc_main", catalog = "doc")
public class ProcessingUserRole extends DocBaseEntity {

  @EmbeddedId
  private ProcessingUserRolePK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("processingUserPK")
  @JoinColumns({
      @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false),
      @JoinColumn(name = "processing_doc_id", referencedColumnName = "processing_doc_id", nullable = false),
      @JoinColumn(name = "step", referencedColumnName = "step", nullable = false)
  })
  private ProcessingUser processingUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("processingRoleId")
  @JoinColumn(name = "processing_role_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocumentRole processingRole;
}

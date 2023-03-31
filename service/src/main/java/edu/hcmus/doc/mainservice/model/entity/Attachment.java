package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attachment", schema = "doc_main", catalog = "doc")
public class Attachment extends DocAbstractEntity{
  private String alfrescoFileId;
  private String alfrescoFolderId;
  private String fileType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDoc = new IncomingDocument();
}

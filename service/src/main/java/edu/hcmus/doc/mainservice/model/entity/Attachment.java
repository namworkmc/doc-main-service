package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.custom.FileTypeConverter;
import edu.hcmus.doc.mainservice.model.enums.FileType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attachment", schema = "doc_main", catalog = "doc")
public class Attachment extends DocAbstractIdEntity {

  @Column(name = "alfresco_file_id", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String alfrescoFileId;

  @Column(name = "alfresco_folder_id", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String alfrescoFolderId;

  @Column(name = "file_type", nullable = false)
  @Convert(converter = FileTypeConverter.class)
  private FileType fileType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDoc = new IncomingDocument();
}

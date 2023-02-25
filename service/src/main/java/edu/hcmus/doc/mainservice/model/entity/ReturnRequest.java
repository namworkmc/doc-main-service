package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.RequestStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "return_request", schema = "doc_main", catalog = "doc")
public class ReturnRequest extends DocAbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false),
      @JoinColumn(name = "processing_doc_id", referencedColumnName = "processing_doc_id", nullable = false),
      @JoinColumn(name = "step", referencedColumnName = "step", nullable = false)
  })
  private ProcessingUser processingUser;

  @Column(name = "reason", nullable = false, length = 200, columnDefinition = "VARCHAR(200) NOT NULL")
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private RequestStatus status;
}

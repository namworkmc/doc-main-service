package edu.hcmus.doc.mainservice.model.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_user", schema = "doc_main", catalog = "doc")
public class ProcessingUser extends DocAbstractIdEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_doc_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocument processingDocument;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "return_request_id", referencedColumnName = "id")
  private ReturnRequest returnRequest;

  @Column(name = "step", nullable = false, columnDefinition = "INT NOT NULL")
  private Integer step;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_method_id", referencedColumnName = "id", updatable = false, nullable = false)
  private ProcessingMethod processingMethod;

  @Column(name = "processing_duration", columnDefinition = "DATE")
  private LocalDate processingDuration;
}

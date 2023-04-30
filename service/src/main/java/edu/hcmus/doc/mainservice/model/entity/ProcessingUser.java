package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.custom.ProcessMethodConverter;
import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
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
@Table(name = "processing_user", schema = "doc_main", catalog = "doc")
public class ProcessingUser extends DocAbstractIdEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user = new User();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processing_doc_id", referencedColumnName = "id", nullable = false)
  private ProcessingDocument processingDocument = new ProcessingDocument();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "return_request_id", referencedColumnName = "id")
  private ReturnRequest returnRequest = new ReturnRequest();

  @Column(name = "step", nullable = false, columnDefinition = "INT NOT NULL")
  private Integer step;

  @Column(name = "process_method", nullable = false)
  @Convert(converter = ProcessMethodConverter.class)
  private ProcessMethod processMethod;
}

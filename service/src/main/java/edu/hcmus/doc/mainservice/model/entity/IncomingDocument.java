package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "incoming_document", schema = "doc_main", catalog = "doc")
public class IncomingDocument extends DocAbstractIdEntity {

  @Column(name = "incoming_number", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String incomingNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_type_id", referencedColumnName = "id", nullable = false)
  private DocumentType documentType = new DocumentType();

  @Column(name = "original_symbol_number", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String originalSymbolNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "distribution_org_id", referencedColumnName = "id", nullable = false)
  private DistributionOrganization distributionOrg = new DistributionOrganization();

  @Column(name = "distribution_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
  private LocalDate distributionDate;

  @Column(name = "arriving_date", nullable = false, columnDefinition = "DATE NOT NULL")
  private LocalDate arrivingDate;

  @Column(name = "arriving_time", nullable = false, columnDefinition = "TIME NOT NULL")
  private LocalTime arrivingTime;

  @Column(name = "summary", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String summary;

  @Enumerated(EnumType.STRING)
  @Column(name = "urgency", nullable = false)
  private Urgency urgency;

  @Enumerated(EnumType.STRING)
  @Column(name = "confidentiality", nullable = false)
  private Confidentiality confidentiality;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", referencedColumnName = "id", nullable = false)
  private Folder folder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sending_level_id", referencedColumnName = "id")
  private SendingLevel sendingLevel = new SendingLevel();
}

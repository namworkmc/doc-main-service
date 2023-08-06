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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "incoming_document", schema = "doc_main", catalog = "doc")
public class IncomingDocument extends DocAbstractIdEntity {

  @NotBlank
  @Column(name = "incoming_number", columnDefinition = "VARCHAR(255) NOT NULL")
  private String incomingNumber;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_type_id", referencedColumnName = "id")
  private DocumentType documentType;

  @NotBlank
  @Column(name = "original_symbol_number", columnDefinition = "VARCHAR(255) NOT NULL")
  private String originalSymbolNumber;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "distribution_org_id", referencedColumnName = "id")
  private DistributionOrganization distributionOrg;

  @NotNull
  @Column(name = "distribution_date", columnDefinition = "TIMESTAMP NOT NULL")
  private LocalDate distributionDate;

  @NotNull
  @Column(name = "arriving_date", columnDefinition = "DATE NOT NULL")
  private LocalDate arrivingDate;

  @NotNull
  @Column(name = "arriving_time", columnDefinition = "TIME NOT NULL")
  private LocalTime arrivingTime;

  @NotBlank
  @Column(name = "summary", columnDefinition = "VARCHAR(255)")
  private String summary;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "urgency")
  private Urgency urgency;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "confidentiality")
  private Confidentiality confidentiality;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", referencedColumnName = "id")
  private Folder folder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sending_level_id", referencedColumnName = "id")
  private SendingLevel sendingLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publishing_department_id", referencedColumnName = "id")
  private Department publishingDepartment;

  @NotBlank
  @Column(name = "name", columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(name = "close_date")
  private LocalDate closeDate;

  @Column(name = "close_username")
  private String closeUsername;

  public void initSendingLevel() {
    if (this.sendingLevel == null) {
      this.sendingLevel = new SendingLevel();
    }
  }

  public void initDocumentType() {
    if (this.documentType == null) {
      this.documentType = new DocumentType();
    }
  }

  public void initDistributionOrg() {
    if (this.distributionOrg == null) {
      this.distributionOrg = new DistributionOrganization();
    }
  }
}

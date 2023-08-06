package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "outgoing_document", schema = "doc_main", catalog = "doc")
public class OutgoingDocument extends DocAbstractIdEntity {

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_type_id", referencedColumnName = "id")
  private DocumentType documentType;

  @NotNull
  @Column(name = "urgency")
  @Enumerated(EnumType.STRING)
  private Urgency urgency;

  @NotNull
  @Column(name = "confidentiality")
  @Enumerated(EnumType.STRING)
  private Confidentiality confidentiality;

  @NotBlank
  @Column(name = "summary")
  private String summary;

  @NotBlank
  @Column(name = "original_symbol_number", nullable = false)
  private String originalSymbolNumber;

  @Column(name = "outgoing_number")
  private String outgoingNumber;

  @NotBlank
  @Column(name = "recipient")
  private String recipient;

  @Column(name = "signer")
  private String signer;

  @FutureOrPresent
  @Column(name = "release_date")
  private LocalDate releaseDate;

  @NotNull
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OutgoingDocumentStatusEnum status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publishing_department_id", referencedColumnName = "id")
  private Department publishingDepartment;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", referencedColumnName = "id")
  private Folder folder;

  @NotBlank
  @Column(name = "name", columnDefinition = "VARCHAR(255)")
  private String name;
}

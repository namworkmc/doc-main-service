package edu.hcmus.doc.mainservice.model.entity.pk;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LinkedDocumentPK implements Serializable {

  private Long incomingDocId;
  private Long outgoingDocId;
}

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
public class ProcessingFlowPK implements Serializable {

  private Long flowVersion;
  private Long docTypeId;
}

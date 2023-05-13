package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentType;
import java.util.List;
import lombok.Data;

@Data
public class TransferDocumentModalSettingDto {
  private List<TransferDocumentMenuConfig> menuConfigs;
  private DocSystemRoleEnum currentRole;
  private TransferDocumentType defaultTransferDocumentType;
  private Integer defaultComponentKey;
}

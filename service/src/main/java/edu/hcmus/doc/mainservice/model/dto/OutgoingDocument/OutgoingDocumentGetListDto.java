package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import lombok.Data;

@Data
public class OutgoingDocumentGetListDto extends DocAbstractDto {
    private Integer ordinalNumber;
    private String name;
    private String outgoingNumber;
    private String originalSymbolNumber;
    private String documentTypeName;
    private String publishingDepartmentName;
    private OutgoingDocumentStatusEnum status;
    private Boolean isDocTransferred;
    private Boolean isDocCollaborator;
    private Boolean isTransferable;
    private String customProcessingDuration;
    private String summary;
}

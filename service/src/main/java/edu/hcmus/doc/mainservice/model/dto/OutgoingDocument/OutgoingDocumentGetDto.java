package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OutgoingDocumentGetDto extends DocAbstractDto {
    private Integer ordinalNumber;
    private String name;
    private String outgoingNumber;
    private String originalSymbolNumber;
    private String recipient;
    private String signer;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private DocumentTypeDto documentType;
    private FolderDto folder;
    private LocalDate releaseDate;
    private DepartmentDto publishingDepartment;
    private OutgoingDocumentStatusEnum status;
    private List<AttachmentDto> attachments = new ArrayList<>();
    private Boolean isDocTransferred;
    private Boolean isDocTransferredByNextUserInFlow;
    private Boolean isDocCollaborator;
    private Boolean isTransferable;
    private Boolean isReleasable;
    private String customProcessingDuration;
}

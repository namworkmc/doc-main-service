package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.dto.SendingLevelDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.IncomingDocument} entity
 */
@Data
public class IncomingDocumentDto extends DocAbstractDto {
    private Integer ordinalNumber;
    private String name;
    private ProcessingStatus status;
    private LocalDate processingDuration;
    private String incomingNumber;
    private DocumentTypeDto documentType;
    private String originalSymbolNumber;
    private DistributionOrganizationDto distributionOrg;
    private LocalDate distributionDate;
    private LocalDate arrivingDate;
    private LocalTime arrivingTime;
    private String summary;
    private SendingLevelDto sendingLevel;
    private FolderDto folder;
    private List<AttachmentDto> attachments = new ArrayList<>();
    private Urgency urgency;
    private Confidentiality confidentiality;
    private Boolean isDocTransferred;
    private Boolean isDocCollaborator;
    private LocalDate closeDate;
    private String closeUsername;
}

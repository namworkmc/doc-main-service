package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OutgoingDocumentGetDto extends DocAbstractDto {
    private String outgoingNumber;
    private String recipient;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private DocumentTypeDto documentType;
    private FolderDto folder;
    private LocalDate releaseDate;
    private DepartmentDto publishingDepartment;
    private OutgoingDocumentStatusEnum status;
}

package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OutgoingDocumentPostDto extends DocAbstractDto  {
    private String name;
    private Long documentType;
    private String originalSymbolNumber;
    private Long folder;
    private Long publishingDepartment;
    private String recipient;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private LocalDate publishingDate;
}

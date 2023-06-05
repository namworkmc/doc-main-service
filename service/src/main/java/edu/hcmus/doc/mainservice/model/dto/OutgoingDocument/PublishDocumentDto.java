package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublishDocumentDto extends DocAbstractDto {
    private String name;
    private String outgoingNumber;
    private String originalSymbolNumber;
    private String recipient;
    private String signer;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private LocalDate releaseDate;
    private Long documentType;
    private Long folder;
    private Long publishingDepartment;
}

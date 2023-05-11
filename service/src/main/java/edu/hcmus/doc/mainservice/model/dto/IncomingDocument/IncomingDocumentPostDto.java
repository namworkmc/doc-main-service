package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IncomingDocumentPostDto {
    private String incomingNumber;
    private Long documentType;
    private String originalSymbolNumber;
    private Long distributionOrg;
    private LocalDate distributionDate;
    private LocalDate arrivingDate;
    private LocalTime arrivingTime;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private Long folder;
}

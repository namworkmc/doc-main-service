package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncomingDocumentPutDto extends DocAbstractDto {

    @NotBlank
    private String name;

    @NotBlank
    private String incomingNumber;

    @NotNull
    private Long documentType;

    @NotBlank
    private String originalSymbolNumber;

    @NotNull
    private Long distributionOrg;

    @NotNull
    private LocalDate distributionDate;

    @NotNull
    private LocalDate arrivingDate;

    @NotNull
    private LocalTime arrivingTime;

    @NotBlank(message = "incomingDocDetailPage.form.summaryRequired")
    private String summary;

    @NotNull
    private Urgency urgency;

    @NotNull
    private Confidentiality confidentiality;

    @NotNull
    private Long folder;
}

package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OutgoingDocumentPostDto extends DocAbstractDto  {
    @NotBlank
    private String name;
    @NotNull
    private Long documentType;
    @NotBlank
    private String originalSymbolNumber;
    @NotNull
    private Long folder;
    @NotNull
    private Long publishingDepartment;
    @NotBlank
    private String recipient;
    @NotBlank(message = "outgoing_doc_detail_page.form.summary_required")
    private String summary;
    @NotNull
    private Urgency urgency;
    @NotNull
    private Confidentiality confidentiality;
    private LocalDate publishingDate;
}

package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import java.util.List;
import lombok.Data;

@Data
public class IncomingDocumentWrapperDto extends DocAbstractDto {
    private List<IncomingDocumentDto> incomingDocumentDtoList;
    private long totalElements;
    private long totalPages;
}

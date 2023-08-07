package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import java.util.List;
import lombok.Data;

@Data
public class OutgoingDocumentWrapperDto extends DocAbstractDto {
    private List<OutgoingDocumentGetListDto> outgoingDocumentGetListDto;
    private long totalElements;
    private long totalPages;
}

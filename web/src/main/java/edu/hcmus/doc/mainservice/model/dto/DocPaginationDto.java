package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocPaginationDto<T> {

    private long totalPages;
    private long totalElements;
    private List<T> payload;
}

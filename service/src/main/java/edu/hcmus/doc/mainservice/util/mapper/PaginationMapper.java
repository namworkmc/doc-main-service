package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaginationMapper {

  default <T> DocPaginationDto<T> toDto(List<T> payload, long totalElements, long totalPages) {
    return DocPaginationDto.<T>builder()
        .payload(payload)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();
  }
}

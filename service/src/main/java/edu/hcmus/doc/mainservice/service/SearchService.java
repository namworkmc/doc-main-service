package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;

public interface SearchService<T, C> {

  DocPaginationDto<T> search(C criteria, int page, int pageSize);
}

package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;

public interface SearchService<R, C> {

  DocPaginationDto<R> search(C criteria, int page, int pageSize);
}

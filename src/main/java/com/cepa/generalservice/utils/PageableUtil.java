package com.cepa.generalservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.exceptions.BadRequestException;

@Component
public class PageableUtil {

    public Pageable getPageable() throws BadRequestException {
        return PageRequest.of(0, 10);
    }

    public Pageable getPageable(PaginationRequest paginationRequest) {
        int page = paginationRequest != null ? paginationRequest.getPage() : 0;
        int size = paginationRequest != null ? paginationRequest.getSize() : 20;
        String sortField = paginationRequest != null ? paginationRequest.getSortField() : "";
        SortType sortType = paginationRequest != null ? paginationRequest.getSortType() : SortType.ASC;

        if (!sortField.isBlank()) {
            Sort sort = sortType == SortType.ASC ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
            return PageRequest.of(page, size, sort);
        }

        return PageRequest.of(page, size);
    }

    public Pageable getPageable(int offset, int limit, String fieldName, SortType sortType) throws BadRequestException {
        if (SortType.ASC.equals(sortType)) {
            return PageRequest.of(offset, limit, Sort.by(fieldName).ascending());
        }
        if (SortType.DESC.equals(sortType)) {
            return PageRequest.of(offset, limit, Sort.by(fieldName).descending());
        }
        throw new BadRequestException("Invalid sort type");
    }

}

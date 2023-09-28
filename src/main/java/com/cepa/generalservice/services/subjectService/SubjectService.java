package com.cepa.generalservice.services.subjectService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;

public interface SubjectService {
    public PaginationResponse<List<SubjectResponse>> getSubjects(Integer page, Integer size, String field, SortType sortType);
}

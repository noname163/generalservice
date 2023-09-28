package com.cepa.generalservice.services.subjectService;

import java.util.List;

import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;

public interface SubjectService {
    public PaginationResponse<List<SubjectResponse>> getSubjects(PaginationRequest paginationRequest);
}

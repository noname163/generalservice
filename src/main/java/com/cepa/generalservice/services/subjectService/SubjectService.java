package com.cepa.generalservice.services.subjectService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.request.SubjectRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;

public interface SubjectService {
    public PaginationResponse<List<SubjectResponse>> getSubjects(Integer page, Integer size, String field,
            SortType sortType, StateType stateType);

    public SubjectResponse createSubject(SubjectRequest subjectRequest);

    public SubjectResponse getSubjectById(Long id);

    public SubjectResponse updateSubject(Long id, SubjectRequest subjectRequest);

    public void deleteSubject(Long id);

    public void activeStateSubject(Long id);
}

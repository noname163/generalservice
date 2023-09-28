package com.cepa.generalservice.services.subjectService.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.mappers.SubjectMapper;
import com.cepa.generalservice.services.subjectService.SubjectService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public PaginationResponse<List<SubjectResponse>> getSubjects(Integer page, Integer size, String field, SortType sortType) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        Page<Subject> listSubject = subjectRepository.findAll(pageable);

        return PaginationResponse.<List<SubjectResponse>>builder()
                .data(subjectMapper.mapEntitiesToDtos(listSubject.getContent()))
                .totalPage(listSubject.getTotalPages())
                .totalRow(listSubject.getTotalElements())
                .build();
    }

}

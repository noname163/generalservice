package com.cepa.generalservice.services.subjectService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.mappers.SubjectMapper;
import com.cepa.generalservice.services.subjectService.SubjectService;
import com.cepa.generalservice.utils.PageableUtil;

public class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private PageableUtil pageableUtil;

    @Mock
    private SubjectMapper subjectMapper;

    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subjectService = SubjectServiceImpl
                .builder()
                .subjectRepository(subjectRepository)
                .subjectMapper(subjectMapper)
                .pageableUtil(pageableUtil)
                .build();
    }

    @Test
    void testGetCombination() {
        
        PaginationRequest paginationRequest = PaginationRequest.builder().build();
        Pageable pageable = Pageable.unpaged();
        Page<Subject> subjectPage = mockSubjectPage(); 
        List<SubjectResponse> expectedResponse = Collections.singletonList(SubjectResponse.builder().build()); 

        when(pageableUtil.getPageable(paginationRequest)).thenReturn(pageable);
        when(subjectRepository.findAll(pageable)).thenReturn(subjectPage);
        when(subjectMapper.mapEntitiesToDtos(subjectPage.getContent())).thenReturn(expectedResponse);

        PaginationResponse<List<SubjectResponse>> result = subjectService.getSubjects(paginationRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result.getData());
    }

    private Page<Subject> mockSubjectPage() {
        List<Subject> subjects = Collections.singletonList(new Subject());
        return new PageImpl<>(subjects);
    }
}

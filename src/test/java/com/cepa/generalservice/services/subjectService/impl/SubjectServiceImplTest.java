package com.cepa.generalservice.services.subjectService.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.mappers.SubjectMapper;
import com.cepa.generalservice.utils.PageableUtil;

public class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private PageableUtil pageableUtil;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSubject_ShouldReturnData() {
        // Arrange
        int page = 1;
        int size = 10;
        String field = "fieldName";
        SortType sortType = SortType.ASC;

        Pageable pageable = PageRequest.of(page - 1, size);
        when(pageableUtil.getPageable(page, size, field,
                sortType)).thenReturn(pageable);

        Page<Subject> pageResult = new PageImpl<>(Collections.emptyList());
        when(subjectRepository.findAll(pageable)).thenReturn(pageResult);

        List<SubjectResponse> subjectResponses = Collections.emptyList();
        when(subjectMapper.mapEntitiesToDtos(pageResult.getContent())).thenReturn(subjectResponses);

        // Act
        PaginationResponse<List<SubjectResponse>> response = subjectService.getSubjects(page, size, field, sortType,
                StateType.ALL);

        // Assert
        assertNotNull(response);
        assertEquals(subjectResponses, response.getData());
        assertEquals(pageResult.getTotalPages(), response.getTotalPage());
        assertEquals(pageResult.getTotalElements(), response.getTotalRow());

        // Verify that the methods were called with the expected parameters
        verify(pageableUtil).getPageable(page, size, field, sortType);
        verify(subjectRepository).findAll(pageable);
        verify(subjectMapper).mapEntitiesToDtos(pageResult.getContent());
    }

}

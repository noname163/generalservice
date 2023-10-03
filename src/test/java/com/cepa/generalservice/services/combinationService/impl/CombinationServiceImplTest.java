package com.cepa.generalservice.services.combinationService.impl;

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
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.mappers.CombinationMapper;
import com.cepa.generalservice.utils.PageableUtil;


public class CombinationServiceImplTest {

    @InjectMocks
    private CombinationServiceImpl combinationService;

    @Mock
    private CombinationRepository combinationRepository;

    @Mock
    private PageableUtil pageableUtil;

    @Mock
    private CombinationMapper combinationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCombination_ShouldReturnData() {
        // Arrange
        int page = 1;
        int size = 10;
        String field = "fieldName";
        SortType sortType = SortType.ASC;

        Pageable pageable = PageRequest.of(page - 1, size);
        when(pageableUtil.getPageable(page, size, field, sortType)).thenReturn(pageable);

        Page<Combination> pageResult = new PageImpl<>(Collections.emptyList());
        when(combinationRepository.findAll(pageable)).thenReturn(pageResult);

        List<CombinationResponse> combinationResponses = Collections.emptyList();
        when(combinationMapper.mapEntitiesToDtos(pageResult.getContent())).thenReturn(combinationResponses);

        // Act
        PaginationResponse<List<CombinationResponse>> response = combinationService.getCombination(page, size, field, sortType);

        // Assert
        assertNotNull(response);
        assertEquals(combinationResponses, response.getData());
        assertEquals(pageResult.getTotalPages(), response.getTotalPage());
        assertEquals(pageResult.getTotalElements(), response.getTotalRow());

        // Verify that the methods were called with the expected parameters
        verify(pageableUtil).getPageable(page, size, field, sortType);
        verify(combinationRepository).findAll(pageable);
        verify(combinationMapper).mapEntitiesToDtos(pageResult.getContent());
    }
}

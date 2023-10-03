package com.cepa.generalservice.services.combinationService.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.mappers.CombinationMapper;
import com.cepa.generalservice.services.combinationService.CombinationService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class CombinationServiceImpl implements CombinationService {

    @Autowired
    private CombinationRepository combinationRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private CombinationMapper combinationMapper;

    @Override
    public PaginationResponse<List<CombinationResponse>> getCombination(Integer page, Integer size, String field,
            SortType sortType) {

        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        Page<Combination> listCombinations = combinationRepository.findAll(pageable);

        return PaginationResponse.<List<CombinationResponse>>builder()
                .data(combinationMapper.mapEntitiesToDtos(listCombinations.getContent()))
                .totalPage(listCombinations.getTotalPages())
                .totalRow(listCombinations.getTotalElements())
                .build();

    }

}

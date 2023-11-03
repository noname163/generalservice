package com.cepa.generalservice.services.combinationService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.request.CombinationRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;

public interface CombinationService {
    public PaginationResponse<List<CombinationResponse>> getCombination(Integer page, Integer size, String field,
            SortType sortType, StateType stateType);

    public void createCombination(CombinationRequest CombinationRequest);

    public CombinationResponse getCombinationById(Long id);

    public CombinationResponse updateCombination(Long id, CombinationRequest combinationRequest);

    public void deleteCombination(Long id);

    public void activeStateCombination(Long id);
}

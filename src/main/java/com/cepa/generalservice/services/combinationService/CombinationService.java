package com.cepa.generalservice.services.combinationService;

import java.util.List;

import com.cepa.generalservice.data.dto.request.PaginationRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;

public interface CombinationService {
    public PaginationResponse<List<CombinationResponse>> getCombination(PaginationRequest paginationRequest);
}

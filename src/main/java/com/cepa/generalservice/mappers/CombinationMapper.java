package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.entities.Combination;

@Component
public class CombinationMapper {
    public CombinationResponse mapEntityToDto(Combination combination) {
        return CombinationResponse.builder()
                .id(combination.getId())
                .name(combination.getName())
                .description(combination.getDescription())
                .build();
    }

    public List<CombinationResponse> mapEntitiesToDtos(List<Combination> combinations) {
        return combinations.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}

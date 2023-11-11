package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.Subject;

@Component
public class CombinationMapper {
    public CombinationResponse mapEntityToDto(Combination combination) {
        List<SubjectResponse> subjectResponses = mapSubjectsToSubjectResponses(combination.getSubject());

        return CombinationResponse.builder()
                .id(combination.getId())
                .name(combination.getName())
                .description(combination.getDescription())
                .subjects(subjectResponses)
                .build();
    }

    public List<CombinationResponse> mapEntitiesToDtos(List<Combination> combinations) {
        return combinations.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private List<SubjectResponse> mapSubjectsToSubjectResponses(List<Subject> subjects) {
        return subjects.stream()
                .map(subject -> SubjectResponse.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .url(subject.getUrl())
                        .description(subject.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public Combination mapDtoToEntity(CombinationResponse combinationResponse) {
        return Combination
                .builder()
                .id(combinationResponse.getId())
                .name(combinationResponse.getName())
                .description(combinationResponse.getDescription()).build();
    }
}

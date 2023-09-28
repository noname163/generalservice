package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;

@Component
public class SubjectMapper {
    public SubjectResponse mapEntityToDto(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .url(subject.getUrl())
                .name(subject.getName())
                .description(subject.getDescription())
                .build();
    }

    public List<SubjectResponse> mapEntitiesToDtos(List<Subject> subjects) {
        return subjects.stream().map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}

package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;

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

    public Subject mapDtoToEntity(SubjectResponse subjectResponse) {
        return Subject
                .builder().id(subjectResponse.getId()).name(subjectResponse.getName()).url(subjectResponse.getUrl())
                .description(subjectResponse.getDescription()).build();
    }
}

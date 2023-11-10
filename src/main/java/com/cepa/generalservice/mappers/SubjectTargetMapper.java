package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.entities.SubjectTarget;

@Component
public class SubjectTargetMapper {
    public SubjectTarget mapDtoToEntity(StudentSubjectTargetRequest subjectTargetRequest) {
        return SubjectTarget
                .builder()
                .subjectId(subjectTargetRequest.getSubjectId())
                .grade(subjectTargetRequest.getGrade())
                .build();
    }

    public List<SubjectTarget> mapDtosToEntities(List<StudentSubjectTargetRequest> studentSubjectTargetRequests) {
        return studentSubjectTargetRequests
                .stream()
                .map(this::mapDtoToEntity)
                .collect(Collectors.toList());
    }
}

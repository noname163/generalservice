package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;

@Component
public class StudentTargetMapper {
    public StudentTargetResponse mapEntityToDto(StudentTarget studentTarget) {
        return StudentTargetResponse
                .builder()
                .id(studentTarget.getId())
                .name(studentTarget.getCombination().getName())
                .grade(studentTarget.getGrade())
                .build();
    }

    public List<StudentTargetResponse> mapEntitiesToDtos(List<StudentTarget> studentTargets) {
        return studentTargets.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}

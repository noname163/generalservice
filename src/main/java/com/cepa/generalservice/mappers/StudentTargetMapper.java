package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.object.interfaces.StudentTargetResponseInterface;

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

    public StudentTargetResponse mapInterfaceToDto(StudentTargetResponseInterface studentTarget) {
        return StudentTargetResponse
                .builder()
                .id(studentTarget.getId())
                .name(studentTarget.getName())
                .grade(studentTarget.getGrade())
                .build();
    }
    public List<StudentTargetResponse> mapInterfacesToDtos(List<StudentTargetResponseInterface> studentTargets) {
        return studentTargets.stream()
                .map(this::mapInterfaceToDto)
                .collect(Collectors.toList());
    }
}

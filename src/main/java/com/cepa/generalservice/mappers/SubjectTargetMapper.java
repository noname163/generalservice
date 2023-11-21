package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.StudentSubjectTargetRequest;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.dto.response.SubjectTargetResponse;
import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.object.interfaces.SubjectTargetResponseInterface;

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

    public SubjectTargetResponse mapToStudentTargetResponse(SubjectTargetResponseInterface subjectTarget) {
        if(subjectTarget==null){
            return null;
        }
        return SubjectTargetResponse
                .builder()
                .id(subjectTarget.getId())
                .grade(subjectTarget.getGrade())
                .name(subjectTarget.getSubjectName())
                .build();
    }
    public List<SubjectTargetResponse> mapToStudentTargetResponses(List<SubjectTargetResponseInterface> subjectTargetResponseInterfaces){
        return subjectTargetResponseInterfaces.stream().map(this::mapToStudentTargetResponse).collect(Collectors.toList());
    }
}

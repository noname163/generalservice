package com.cepa.generalservice.services.studentService.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.StudentPublicResponse;
import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.data.dto.response.StudentTargetResponse;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.mappers.StudentMapper;
import com.cepa.generalservice.mappers.StudentTargetMapper;
import com.cepa.generalservice.mappers.UserInformationMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.studentService.StudentInformationService;
import com.cepa.generalservice.services.studentService.StudentTargetService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class StudentInformationServiceImpl implements StudentInformationService {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private StudentTargetMapper studentTargetMapper;
    @Autowired
    private UserInformationMapper userInformationMapper;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private StudentTargetService studentTargetService;
    @Autowired
    private SecurityContextService securityContextService;

    @Override
    public StudentResponse getStudentInformation() {
        UserInformation currentUser = securityContextService.getCurrentUser();
        UserInformation userInformation = userService.getUserByEmail(currentUser.getEmail());
        StudentResponse studentResponse = studentMapper.mapEntityToDto(userInformation);
        studentResponse.setTargets(studentTargetService.getStudentTargetsOfCurrentStudent());
        return studentResponse;
    }

    @Override
    public PaginationResponse<List<StudentResponse>> getStudents(Integer page, Integer size, String field,
            SortType sortType, UserStatus userStatus) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);
        Page<UserInformation> listStudent;
        if (userStatus != null && userStatus != UserStatus.ALL) {
            listStudent = userInformationRepository.findAllByRoleAndStatus(pageable, Role.STUDENT, userStatus);
        } else {
            listStudent = userInformationRepository.findAllByRole(pageable, Role.STUDENT);
        }
        List<StudentResponse> studentResponses = new ArrayList<>();
        for (UserInformation userInformation : listStudent) {
            List<StudentTarget> filteredTargets = userInformation.getStudentTargets().stream()
                    .filter(target -> target.getStateType() == StateType.TRUE)
                    .collect(Collectors.toList());
            StudentResponse studentResponse = studentMapper.mapEntityToDto(userInformation);
            studentResponse.setTargets(studentTargetMapper.mapEntitiesToDtos(filteredTargets));
            studentResponses.add(studentResponse);
        }
        return PaginationResponse.<List<StudentResponse>>builder()
                .data(studentResponses)
                .totalPage(listStudent.getTotalPages())
                .totalRow(listStudent.getTotalElements())
                .build();
    }

    @Override
    public StudentPublicResponse getStudentInformationByEmail(String email) {
        UserInformation userInformation = userService.getUserByEmail(email);
        List<StudentTargetResponse> studentTargetResponses = studentTargetService
                .getStudentTargetsById(userInformation.getId());
        return StudentPublicResponse
                .builder()
                .userResponse(userInformationMapper.mapEntityToDto(userInformation))
                .targets(studentTargetResponses)
                .build();

    }

}

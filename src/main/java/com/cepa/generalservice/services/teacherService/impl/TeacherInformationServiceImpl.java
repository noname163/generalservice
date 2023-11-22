package com.cepa.generalservice.services.teacherService.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.mappers.TeacherMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class TeacherInformationServiceImpl implements TeacherInformationService {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private SecurityContextService securityContextService;

    @Override
    public TeacherResponse getTeacherInformation() {
        UserInformation currentUser = securityContextService.getCurrentUser();
        UserInformation userInformation = userService.getUserByEmail(currentUser.getEmail());
        TeacherResponse teacherResponse = teacherMapper.mapEntityToDto(userInformation);
        List<String> subjects = new ArrayList<>();
        List<Subject> subjectsObject = userInformation.getTeachers().getSubjects();
        for (Subject subject : subjectsObject) {
            subjects.add(subject.getName());
        }
        teacherResponse.setSubject(subjects);
        return teacherResponse;
    }

    @Override
    public PaginationResponse<List<TeacherResponse>> getTeachers(Integer page, Integer size, String field,
            SortType sortType, UserStatus userStatus) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);
        Page<UserInformation> listTeacher;
        if (userStatus != null && userStatus != UserStatus.ALL) {

            listTeacher = userInformationRepository.findAllByRoleAndStatus(pageable, Role.TEACHER, userStatus);
        } else {

            listTeacher = userInformationRepository.findAllByRole(pageable, Role.TEACHER);
        }

        return PaginationResponse.<List<TeacherResponse>>builder()
                .data(teacherMapper.mapEntitiesToDtos(listTeacher.getContent()))
                .totalPage(listTeacher.getTotalPages())
                .totalRow(listTeacher.getTotalElements())
                .build();
    }

    @Override
    public TeacherResponse getTeacherInformationByEmail(String email) {
        UserInformation userInformation = userService.getUserByEmail(email);
        TeacherResponse teacherResponse = teacherMapper.mapEntityToDto(userInformation);
        List<String> subjects = new ArrayList<>();
        List<Subject> subjectsObject = userInformation.getTeachers().getSubjects();
        for (Subject subject : subjectsObject) {
            subjects.add(subject.getName());
        }
        teacherResponse.setSubject(subjects);
        return teacherResponse;
    }

}

package com.cepa.generalservice.services.teacherService.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.EditTeacherRequest;
import com.cepa.generalservice.data.dto.response.CloudinaryUrl;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponseForAdmin;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseForAdminInterface;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.mappers.TeacherMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;
import com.cepa.generalservice.services.uploadservice.UploadService;
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
    private TeacherRepository teacherRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public TeacherResponse getTeacherInformation() {
        UserInformation currentUser = securityContextService.getCurrentUser();
        TeacherResponse teacherResponse = teacherMapper.mapEntityToDto(currentUser);
        Teacher teacher = currentUser.getTeachers();
        List<String> subjects = new ArrayList<>();
        List<Subject> subjectsObject = teacher.getSubjects();
        for (Subject subject : subjectsObject) {
            subjects.add(subject.getName());
        }
        teacherResponse.setSubject(subjects);
        teacherResponse.setIsVerify(teacher.getIsValidation());
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
        Teacher teacher = userInformation.getTeachers();
        List<Subject> subjectsObject = teacher.getSubjects();
        for (Subject subject : subjectsObject) {
            subjects.add(subject.getName());
        }
        teacherResponse.setSubject(subjects);
        teacherResponse.setIsVerify(teacher.getIsValidation());
        return teacherResponse;
    }

    @Override
    public void editTeacherInformation(EditTeacherRequest editTeacherRequest, MultipartFile indentify) {
        Teacher teacher = securityContextService.getCurrentUser().getTeachers();

        if (indentify != null) {
            CloudinaryUrl cloudinaryUrl = uploadService.uploadMedia(indentify);
            teacher.setIdentify(cloudinaryUrl.getUrl());
            teacher.setPublicId(cloudinaryUrl.getPublicId());
        }

        teacher.setCardNumber(Optional.ofNullable(editTeacherRequest.getCardNumber()).orElse(teacher.getCardNumber()));
        teacher.setUpdateDate(LocalDateTime.now());
        if (editTeacherRequest.getSubjectId() != null && !editTeacherRequest.getSubjectId().isEmpty()) {
            List<Subject> subjects = subjectRepository.findByIdIn(editTeacherRequest.getSubjectId())
                    .orElseThrow(() -> new BadRequestException(
                            "Cannot found subject with ID: " + editTeacherRequest.getSubjectId()));
            teacher.setSubjects(subjects);
        }

        teacherRepository.save(teacher);
    }

    @Override
    public PaginationResponse<List<TeacherResponseForAdmin>> getListVerifyTeacher(Integer page, Integer size,
            String field,
            SortType sortType) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);
        Page<TeacherResponseForAdminInterface> teacherResponses = teacherRepository.findTeachersForAdmin(pageable);

        return PaginationResponse.<List<TeacherResponseForAdmin>>builder()
                .data(teacherMapper.mapToTeacherResponseForAdminList(teacherResponses.getContent()))
                .totalPage(teacherResponses.getTotalPages())
                .totalRow(teacherResponses.getTotalElements())
                .build();
    }

}

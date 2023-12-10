package com.cepa.generalservice.services.teacherService.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.constants.Role;
import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.EditTeacherRequest;
import com.cepa.generalservice.data.dto.request.SendMailRequest;
import com.cepa.generalservice.data.dto.request.VerifyRequest;
import com.cepa.generalservice.data.dto.response.CloudinaryUrl;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponse;
import com.cepa.generalservice.data.dto.response.TeacherResponseForAdmin;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseForAdminInterface;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseInterface;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TeacherRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.mappers.TeacherMapper;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;
import com.cepa.generalservice.services.teacherService.TeacherInformationService;
import com.cepa.generalservice.services.uploadservice.UploadService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class TeacherInformationServiceImpl implements TeacherInformationService {

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
    @Autowired
    private SendEmailService sendEmailService;

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
        Page<TeacherResponseInterface> listTeacherInterface;
        if (userStatus != null && userStatus != UserStatus.ALL) {
            listTeacherInterface = teacherRepository.getTeachersByStatus(userStatus, pageable);
        } else {
            listTeacherInterface = teacherRepository.getTeacherResponses(pageable);
        }

        Map<String, TeacherResponse> teacherMapByEmail = new HashMap<>();

        for (TeacherResponseInterface teacherResponse : listTeacherInterface.getContent()) {
            String email = teacherResponse.getEmail();
            TeacherResponse existingTeacher = teacherMapByEmail.get(email);

            if (existingTeacher != null) {
                // Add subjects to the existing TeacherResponseForAdmin
                List<String> mergedSubjects = new ArrayList<>(existingTeacher.getSubject());
                mergedSubjects.addAll(teacherResponse.getSubject());
                existingTeacher.setSubject(mergedSubjects);
            } else {
                // Add a new entry to the map
                teacherMapByEmail.put(email, teacherMapper.mapToTeacherResponse(teacherResponse));
            }
        }

        List<TeacherResponse> mergedTeacherResponses = new ArrayList<>(teacherMapByEmail.values());
        return PaginationResponse.<List<TeacherResponse>>builder()
                .data(mergedTeacherResponses)
                .totalPage(listTeacherInterface.getTotalPages())
                .totalRow(listTeacherInterface.getTotalElements())
                .build();
    }

    @Override
    public TeacherResponse getTeacherInformationByEmail(String email) {
        UserInformation userInformation = userInformationRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Not exist user with email " + email));
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
        teacher.setIsValidation(false);
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

        Map<String, TeacherResponseForAdmin> teacherMapByEmail = new HashMap<>();

        for (TeacherResponseForAdminInterface teacherResponse : teacherResponses.getContent()) {
            String email = teacherResponse.getEmail();
            TeacherResponseForAdmin existingTeacher = teacherMapByEmail.get(email);

            if (existingTeacher != null) {
                // Add subjects to the existing TeacherResponseForAdmin
                List<String> mergedSubjects = new ArrayList<>(existingTeacher.getSubject());
                mergedSubjects.addAll(teacherResponse.getSubject());
                existingTeacher.setSubject(mergedSubjects);
            } else {
                // Add a new entry to the map
                teacherMapByEmail.put(email, teacherMapper.mapToTeacherResponseForAdmin(teacherResponse));
            }
        }

        List<TeacherResponseForAdmin> mergedTeacherResponses = new ArrayList<>(teacherMapByEmail.values());

        return PaginationResponse.<List<TeacherResponseForAdmin>>builder()
                .data(mergedTeacherResponses)
                .totalPage(teacherResponses.getTotalPages())
                .totalRow(teacherResponses.getTotalElements())
                .build();
    }

    @Override
    public TeacherResponseForAdmin getTeacherVerifyById(Long id) {
        UserInformation teacherInformation = userInformationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cannot found teacher with id " + id));

        Teacher teacher = teacherInformation.getTeachers();
        List<Subject> teacherSubject = teacher.getSubjects();
        List<String> subjects = new ArrayList<>();
        for (Subject subject : teacherSubject) {
            subjects.add(subject.getName());
        }
        TeacherResponseForAdmin teacherResponseForAdmin = teacherMapper
                .mapEntityToTeacherResponseForAdmin(teacherInformation);
        teacherResponseForAdmin.setIndentify(teacher.getIdentify());
        teacherResponseForAdmin.setSubject(subjects);
        return teacherResponseForAdmin;
    }

    @Override
    public void verifyTeacher(VerifyRequest verifyRequest) {
        UserInformation teacherInformation = userInformationRepository.findById(verifyRequest.getTeacherId())
                .orElseThrow(
                        () -> new BadRequestException("Cannot found teacher with id " + verifyRequest.getTeacherId()));
        Teacher teacher = teacherInformation.getTeachers();
        String mailTemplate = "";
        UserInformation userInformation = teacher.getInformation();
        if (Boolean.TRUE.equals(verifyRequest.getVerify())) {
            teacher.setIsValidation(true);
            teacherRepository.save(teacher);
            mailTemplate = VerificationTokenTemplate.verifySuccessEmail(userInformation.getFullName());
        } else {
            mailTemplate = VerificationTokenTemplate.verifyFailEmail(userInformation.getFullName(),
                    verifyRequest.getReason());
        }
        sendEmailService.sendMailService(SendMailRequest
                .builder()
                .subject("Thông báo hệ thống")
                .mailTemplate(mailTemplate)
                .userEmail(userInformation.getEmail())
                .build());

    }

}

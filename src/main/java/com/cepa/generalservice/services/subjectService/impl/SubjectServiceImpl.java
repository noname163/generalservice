package com.cepa.generalservice.services.subjectService.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.request.SubjectRequest;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.exceptions.DataConfilictException;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.mappers.SubjectMapper;
import com.cepa.generalservice.services.subjectService.SubjectService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public PaginationResponse<List<SubjectResponse>> getSubjects(Integer page, Integer size, String field,
            SortType sortType, StateType stateType) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);
        Page<Subject> listSubject;

        switch (stateType) {
            case ALL:
                listSubject = subjectRepository.findAll(pageable);
                break;
            case TRUE:
                listSubject = subjectRepository.findAllByStateTrue(pageable);
                break;
            case FALSE:
                listSubject = subjectRepository.findAllByStateFalse(pageable);
                break;
            default:
                listSubject = subjectRepository.findAll(pageable);
                break;
        }

        return PaginationResponse.<List<SubjectResponse>>builder()
                .data(subjectMapper.mapEntitiesToDtos(listSubject.getContent()))
                .totalPage(listSubject.getTotalPages())
                .totalRow(listSubject.getTotalElements())
                .build();
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {

        subjectRepository.findByName(subjectRequest.getName()).ifPresent(subjectInformation -> {
            throw new DataConfilictException("Subject name already exist.");
        });
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setUrl(subjectRequest.getUrl());
        subject.setDescription(subjectRequest.getDescription());

        subject = subjectRepository.save(subject);

        return subjectMapper.mapEntityToDto(subject);
    }

    @Override
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
        return subjectMapper.mapEntityToDto(subject);
    }

    @Override
    public SubjectResponse updateSubject(Long id, SubjectRequest subjectRequest) {
        Subject existingSubject = subjectRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));

        subjectRepository.findByName(subjectRequest.getName()).ifPresent(subjectInformation -> {
            throw new DataConfilictException("Subject name already exist.");
        });
        existingSubject.setName(subjectRequest.getName());
        existingSubject.setUrl(subjectRequest.getUrl());
        existingSubject.setDescription(subjectRequest.getDescription());

        existingSubject = subjectRepository.save(existingSubject);

        return subjectMapper.mapEntityToDto(existingSubject);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));

        subject.setState(false);
        subjectRepository.save(subject);
    }

    @Override
    public void activeStateSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));

        subject.setState(true);
        subjectRepository.save(subject);
    }

}

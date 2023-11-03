package com.cepa.generalservice.services.combinationService.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.request.CombinationRequest;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.DataConfilictException;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.mappers.CombinationMapper;
import com.cepa.generalservice.services.combinationService.CombinationService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class CombinationServiceImpl implements CombinationService {

    @Autowired
    private CombinationRepository combinationRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private CombinationMapper combinationMapper;

    @Override
    public PaginationResponse<List<CombinationResponse>> getCombination(Integer page, Integer size, String field,
            SortType sortType,StateType stateType) {

        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        Page<Combination> listCombinations;

        switch (stateType) {
            case ALL:
                listCombinations = combinationRepository.findAll(pageable);
                break;
            case TRUE:
                listCombinations = combinationRepository.findAllByStateTrue(pageable);
                break;
            case FALSE:
                listCombinations = combinationRepository.findAllByStateFalse(pageable);
                break;
            default:
                listCombinations = combinationRepository.findAll(pageable);
                break;
        }

        return PaginationResponse.<List<CombinationResponse>>builder()
                .data(combinationMapper.mapEntitiesToDtos(listCombinations.getContent()))
                .totalPage(listCombinations.getTotalPages())
                .totalRow(listCombinations.getTotalElements())
                .build();

    }

    @Override
    @Transactional
    public void createCombination(CombinationRequest combinationRequest) {
        combinationRepository.findByName(combinationRequest.getName()).ifPresent(combinationInformation -> {
            throw new DataConfilictException("Combination name already exist.");
        });

        List<Subject> subjects = subjectRepository.findByIdInAndStateTrue(combinationRequest.getSubjectIds())
                .orElseThrow(() -> new BadRequestException(
                        "Cannot found subject with ID: " + combinationRequest.getSubjectIds().toString()));

        // handle check subject empty, not exist and duplicate
        if (subjects.isEmpty()) {
            throw new BadRequestException("SubjectIds not found: "
                    + combinationRequest.getSubjectIds().toString());
        } else if (subjects.size() != combinationRequest.getSubjectIds().size()) {
            List<Long> existingSubjectIds = subjects.stream()
                    .map(Subject::getId)
                    .collect(Collectors.toList());
            List<Long> nonExistentSubjectIds = combinationRequest.getSubjectIds().stream()
                    .filter(id -> !existingSubjectIds.contains(id))
                    .collect(Collectors.toList());
            if (nonExistentSubjectIds.isEmpty()) {
                throw new DataConfilictException("Duplicate subject IDs are not allowed.");
            } else {

                throw new NotFoundException("Subjects with IDs " + nonExistentSubjectIds + " do not exist.");
            }
        }

        combinationRepository.save(Combination
                .builder()
                .name(combinationRequest.getName())
                .description(combinationRequest.getDescription())
                .subject(subjects)
                .build());
    }

    @Override
    public CombinationResponse getCombinationById(Long id) {
        Combination combination = combinationRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));
        return combinationMapper.mapEntityToDto(combination);
    }

    @Override
    @Transactional
    public CombinationResponse updateCombination(Long id, CombinationRequest combinationRequest) {
        Combination existingCombination = combinationRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));

        combinationRepository.findByName(combinationRequest.getName()).ifPresent(combinationInformation -> {
            if (combinationInformation.getId() != id) {
                throw new DataConfilictException("Combination name already exists.");
            }
        });

        List<Subject> subjects = subjectRepository.findByIdInAndStateTrue(combinationRequest.getSubjectIds())
                .orElseThrow(() -> new NotFoundException(
                        "Cannot found subject with ID: " + combinationRequest.getSubjectIds().toString()));
        if (subjects.isEmpty()) {
            throw new BadRequestException("SubjectIds not found: "
                    + combinationRequest.getSubjectIds().toString());
        } else if (subjects.size() != combinationRequest.getSubjectIds().size()) {
            List<Long> existingSubjectIds = subjects.stream()
                    .map(Subject::getId)
                    .collect(Collectors.toList());
            List<Long> nonExistentSubjectIds = combinationRequest.getSubjectIds().stream()
                    .filter(subjectId -> !existingSubjectIds.contains(subjectId))
                    .collect(Collectors.toList());
            if (nonExistentSubjectIds.isEmpty()) {
                throw new DataConfilictException("Duplicate subject IDs are not allowed.");
            } else {

                throw new NotFoundException("Subjects with IDs " + nonExistentSubjectIds + " do not exist.");
            }
        }

        existingCombination.setName(combinationRequest.getName());
        existingCombination.setDescription(combinationRequest.getDescription());
        existingCombination.setSubject(subjects);

        existingCombination = combinationRepository.save(existingCombination);

        return combinationMapper.mapEntityToDto(existingCombination);
    }

    @Override
    public void deleteCombination(Long id) {
        Combination combination = combinationRepository.findByIdAndStateTrue(id)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));

        combination.setState(false);
        combinationRepository.save(combination);
    }

    @Override
    public void activeStateCombination(Long id) {
        Combination combination = combinationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));

        List<Long> subjectIds = combination.getSubject().stream()
                .map(Subject::getId)
                .collect(Collectors.toList());

        List<Subject> subjects = subjectRepository.findByIdInAndStateTrue(subjectIds)
                .orElseThrow(() -> new BadRequestException(
                        "Cannot found subject with ID: " + subjectIds));

        List<Long> existingSubjectIds = subjects.stream()
                .map(Subject::getId)
                .collect(Collectors.toList());

        List<Long> nonExistentSubjectIds = subjectIds.stream()
                .filter(subjectId -> !existingSubjectIds.contains(subjectId))
                .collect(Collectors.toList());

        if (!nonExistentSubjectIds.isEmpty()) {
            throw new NotFoundException(
                    "Subjects with IDs " + nonExistentSubjectIds + " do not exist or are not active.");
        }
        combination.setState(true);
        combinationRepository.save(combination);
    }

}

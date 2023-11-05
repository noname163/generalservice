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
            SortType sortType, StateType stateType) {

        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        Page<Combination> listCombinations = combinationRepository.findAll(pageable);
        if (stateType.equals(StateType.TRUE)) {
            listCombinations = combinationRepository.findAllByState(pageable, true);
        } else if (stateType.equals(StateType.FALSE)) {
            listCombinations = combinationRepository.findAllByState(pageable, false);
        }

        return PaginationResponse.<List<CombinationResponse>>builder()
                .data(combinationMapper.mapEntitiesToDtos(listCombinations.getContent()))
                .totalPage(listCombinations.getTotalPages())
                .totalRow(listCombinations.getTotalElements())
                .build();

    }

    private void checkCombinationNameConflict(Long combinationId, String combinationName) {
        combinationRepository.findByName(combinationName).ifPresent(existingCombination -> {
            if (combinationId == null || (existingCombination.getId() != combinationId)) {
                throw new DataConfilictException("Combination name already exists.");
            }
        });
    }

    private List<Subject> validateSubjects(List<Long> subjectIds) {
        List<Subject> subjects = subjectRepository.findByIdInAndState(subjectIds, true)
                .orElseThrow(() -> new BadRequestException(
                        "Cannot find subjects with ID: " + subjectIds.toString()));

        if (subjects.isEmpty()) {
            throw new BadRequestException("SubjectIds not found: " + subjectIds.toString());
        } else if (subjects.size() != subjectIds.size()) {
            List<Long> existingSubjectIds = subjects.stream()
                    .map(Subject::getId)
                    .collect(Collectors.toList());
            List<Long> nonExistentSubjectIds = subjectIds.stream()
                    .filter(id -> !existingSubjectIds.contains(id))
                    .collect(Collectors.toList());
            if (nonExistentSubjectIds.isEmpty()) {
                throw new DataConfilictException("Duplicate subject IDs are not allowed.");
            } else {
                throw new NotFoundException("Subjects with IDs " + nonExistentSubjectIds + " do not exist.");
            }
        }

        return subjects;
    }

    @Override
    @Transactional
    public void createCombination(CombinationRequest combinationRequest) {
        checkCombinationNameConflict(null, combinationRequest.getName());

        List<Subject> subjects = validateSubjects(combinationRequest.getSubjectIds());

        combinationRepository.save(Combination
                .builder()
                .name(combinationRequest.getName())
                .description(combinationRequest.getDescription())
                .subject(subjects)
                .build());
    }

    @Override
    public CombinationResponse getCombinationById(Long id) {
        Combination combination = combinationRepository.findByIdAndState(id, true)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));
        return combinationMapper.mapEntityToDto(combination);
    }

    @Override
    @Transactional
    public CombinationResponse updateCombination(Long id, CombinationRequest combinationRequest) {
        Combination existingCombination = combinationRepository.findByIdAndState(id, true)
                .orElseThrow(() -> new NotFoundException("Combination not found with id: " + id));

        checkCombinationNameConflict(id, combinationRequest.getName());

        List<Subject> subjects = validateSubjects(combinationRequest.getSubjectIds());

        existingCombination.setName(combinationRequest.getName());
        existingCombination.setDescription(combinationRequest.getDescription());
        existingCombination.setSubject(subjects);

        existingCombination = combinationRepository.save(existingCombination);

        return combinationMapper.mapEntityToDto(existingCombination);
    }

    @Override
    public void deleteCombination(Long id) {
        Combination combination = combinationRepository.findByIdAndState(id, true)
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

        List<Subject> subjects = subjectRepository.findByIdInAndState(subjectIds, true)
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

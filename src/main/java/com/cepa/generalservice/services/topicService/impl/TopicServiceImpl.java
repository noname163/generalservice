package com.cepa.generalservice.services.topicService.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.data.dto.response.TopicResponse;
import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Topic;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.SubjectRepository;
import com.cepa.generalservice.data.repositories.TopicRepository;
import com.cepa.generalservice.exceptions.NotFoundException;
import com.cepa.generalservice.mappers.SubjectMapper;
import com.cepa.generalservice.mappers.TopicMapper;
import com.cepa.generalservice.services.topicService.TopicService;
import com.cepa.generalservice.utils.PageableUtil;

import lombok.Builder;

@Service
@Builder
public class TopicServiceImpl implements TopicService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PageableUtil pageableUtil;
    @Autowired
    private TopicMapper topicMapper;

    @Override
    public PaginationResponse<List<TopicResponse>> getTopics(Integer page, Integer size, String field,
            SortType sortType) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);
        Page<Topic> listTopic = topicRepository.findAll(pageable);

        return PaginationResponse.<List<TopicResponse>>builder()
                .data(topicMapper.mapEntitiesToDtos(listTopic.getContent()))
                .totalPage(listTopic.getTotalPages())
                .totalRow(listTopic.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<List<TopicResponse>> getTopicsBySubject(Integer page, Integer size, String field,
            SortType sortType, Long id) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        // Retrieve the subject by its ID
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));

        // Retrieve topics for the specific subject
        Page<Topic> listTopic = topicRepository.findBySubject(subject, pageable);

        return PaginationResponse.<List<TopicResponse>>builder()
                .data(topicMapper.mapEntitiesToDtos(listTopic.getContent()))
                .totalPage(listTopic.getTotalPages())
                .totalRow(listTopic.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<List<TopicResponse>> searchTopicByName(Integer page, Integer size, String field,
            SortType sortType, String name) {
        Pageable pageable = pageableUtil.getPageable(page, size, field, sortType);

        Page<Topic> listTopic;
        if (name == "") {
            listTopic = topicRepository.findAll(pageable);
        } else {

            listTopic = topicRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        return PaginationResponse.<List<TopicResponse>>builder()
                .data(topicMapper.mapEntitiesToDtos(listTopic.getContent()))
                .totalPage(listTopic.getTotalPages())
                .totalRow(listTopic.getTotalElements())
                .build();
    }

}

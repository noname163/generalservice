package com.cepa.generalservice.services.topicService;

import java.util.List;

import com.cepa.generalservice.data.constants.SortType;

import com.cepa.generalservice.data.dto.response.PaginationResponse;

import com.cepa.generalservice.data.dto.response.TopicResponse;

public interface TopicService {
    public PaginationResponse<List<TopicResponse>> getTopics(Integer page, Integer size, String field,
            SortType sortType);

    public PaginationResponse<List<TopicResponse>> getTopicsBySubject(Integer page, Integer size, String field,
            SortType sortType, Long id);

    public PaginationResponse<List<TopicResponse>> searchTopicByName(Integer page, Integer size, String field,
            SortType sortType, String name);
}

package com.cepa.generalservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.response.TopicResponse;
import com.cepa.generalservice.data.entities.Topic;

@Component
public class TopicMapper {
    public TopicResponse mapEntityToDto(Topic topic) {
        return TopicResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .build();
    }

    public List<TopicResponse> mapEntitiesToDtos(List<Topic> topics) {
        return topics.stream().map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public Topic mapDtoToEntity(TopicResponse topicResponse) {
        return Topic
                .builder().id(topicResponse.getId()).name(topicResponse.getName()).build();
    }
}

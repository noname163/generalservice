package com.cepa.generalservice.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Subject;
import com.cepa.generalservice.data.entities.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    public Page<Topic> findBySubject(Subject subject, Pageable pageable);

    public Page<Topic> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

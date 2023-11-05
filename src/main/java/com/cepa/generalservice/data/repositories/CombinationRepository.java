package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.Subject;

public interface CombinationRepository extends JpaRepository<Combination, Long> {
    public Optional<List<Combination>> findByIdIn(List<Long> ids);

    public Optional<List<Combination>> findByIdIn(Set<Long> ids);

    public Page<Combination> findAllByState(Pageable pageable, Boolean state);

    public Optional<Combination> findByName(String subjectName);

    public Optional<List<Combination>> findByStateTrueAndSubjectContaining(Subject subject);

    public Optional<Combination> findByIdAndState(Long id, Boolean state);
}

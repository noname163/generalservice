package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    public Page<Subject> findAllByStateTrue(Pageable pageable);

    public Page<Subject> findAllByStateFalse(Pageable pageable);

    public Optional<List<Subject>> findByIdIn(List<Long> ids);

    public Optional<Subject> findByName(String subjectName);

    public Optional<Subject> findByIdAndStateTrue(Long id);
}

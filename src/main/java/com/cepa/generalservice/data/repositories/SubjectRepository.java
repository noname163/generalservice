package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
    public Optional<List<Subject>> findByIdIn(List<Long> ids);
}

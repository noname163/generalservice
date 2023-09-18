package com.cepa.generalservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.SubjectTarget;

public interface SubjectTargetRepository extends JpaRepository<SubjectTarget, Long> {
    
}

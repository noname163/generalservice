package com.cepa.generalservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.StudentTarget;

public interface StudentTargetRepository extends JpaRepository<StudentTarget,Long> {
}

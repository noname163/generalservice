package com.cepa.generalservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Target;

public interface TargetRepository extends JpaRepository<Target, Long> {
    
}

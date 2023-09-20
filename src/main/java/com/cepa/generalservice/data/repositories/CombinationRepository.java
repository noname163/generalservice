package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.Combination;

public interface CombinationRepository extends JpaRepository<Combination, Long> {
    public Optional<List<Combination>> findByIdIn(List<Long> ids);
}

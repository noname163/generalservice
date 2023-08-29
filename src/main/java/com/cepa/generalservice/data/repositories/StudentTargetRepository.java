package com.cepa.generalservice.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;

public interface StudentTargetRepository extends JpaRepository<StudentTarget,Long> {
    public List<StudentTarget> findByStudentInformation(UserInformation studentInformation);
}

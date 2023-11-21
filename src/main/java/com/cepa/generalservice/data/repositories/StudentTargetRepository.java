package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;

public interface StudentTargetRepository extends JpaRepository<StudentTarget, Long> {
    public List<StudentTarget> findByStudentInformation(UserInformation studentInformation);

    public List<StudentTarget> findByStudentInformationAndStateType(UserInformation studentInformation,
            StateType stateType);

    public Optional<List<StudentTarget>> findByIdIn(List<Long> ids);

    public Optional<StudentTarget> findByIdAndStateType(Long id, StateType stateType);
}

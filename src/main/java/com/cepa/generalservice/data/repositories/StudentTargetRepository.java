package com.cepa.generalservice.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.object.interfaces.StudentTargetResponseInterface;

public interface StudentTargetRepository extends JpaRepository<StudentTarget, Long> {
    public List<StudentTarget> findByStudentInformation(UserInformation studentInformation);

    public List<StudentTarget> findByStudentInformationAndStateType(UserInformation studentInformation,
            StateType stateType);

    public Optional<List<StudentTarget>> findByIdIn(List<Long> ids);

    public Optional<StudentTarget> findByIdAndStateType(Long id, StateType stateType);

    @Query("SELECT st.id AS id, st.grade AS grade, st.combination.name AS name " +
            "FROM StudentTarget st " +
            "WHERE st.studentInformation.id = :studentId " +
            "AND st.stateType = com.cepa.generalservice.data.constants.StateType.TRUE")
    List<StudentTargetResponseInterface> getStudentTargetsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END FROM StudentTarget st WHERE st.studentInformation.id = :studentId AND st.id = :id")
    Boolean existByStudentInformationIdAndId(@Param("studentId") Long studentId, @Param("id") Long id);

    Optional<StudentTarget> findByStudentInformationIdAndId(Long studentId, Long id);

}

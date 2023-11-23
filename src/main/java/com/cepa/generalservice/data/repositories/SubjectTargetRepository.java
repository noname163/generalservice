package com.cepa.generalservice.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cepa.generalservice.data.entities.SubjectTarget;
import com.cepa.generalservice.data.object.interfaces.SubjectTargetResponseInterface;

public interface SubjectTargetRepository extends JpaRepository<SubjectTarget, Long> {

    @Query("SELECT sub.name AS subjectName, s.grade AS grade, s.id AS id, " +
            "sub.id AS subjectId " +
            "FROM SubjectTarget s " +
            "JOIN Subject sub ON s.subjectId = sub.id " +
            "WHERE s.studentTarget.id = :studentTargetId ")
    List<SubjectTargetResponseInterface> getSubjectTargetsByStudentTargetIdAndUserId(
            @Param("studentTargetId") Long studentTargetId);

    List<SubjectTarget> findByStudentTargetId(Long studentTargetId);

}

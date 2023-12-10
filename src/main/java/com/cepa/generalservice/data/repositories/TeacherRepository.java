package com.cepa.generalservice.data.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseForAdminInterface;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseInterface;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByInformationId(long id);

    @Query("SELECT " +
            "t.information.id AS id, " +
            "t.information.email AS email, " +
            "t.information.fullName AS fullName, " +
            "t.information.imageURL AS url, " +
            "t.information.createDate AS createDate, " +
            "t.information.status AS status, " +
            "s.name AS subject, " +
            "t.information.dateOfBirth AS dateOfBirth, " +
            "t.identify AS identify " +
            "FROM Teacher t " +
            "LEFT JOIN t.subjects s " +
            "WHERE (t.identify IS NOT NULL AND t.identify <> '') " +
            "AND t.isValidation = false")
    Page<TeacherResponseForAdminInterface> findTeachersForAdmin(Pageable pageable);

     @Query("SELECT " +
            "t.information.id AS id, " +
            "t.information.email AS email, " +
            "t.information.fullName AS fullName, " +
            "t.information.imageURL AS url, " +
            "t.information.createDate AS createDate, " +
            "t.information.description AS description, " +
            "t.information.status AS status, " +
            "s.name AS subject, " +
            "t.information.dateOfBirth AS dateOfBirth, " +
            "t.isValidation AS isVerify " +
            "FROM Teacher t " +
            "LEFT JOIN t.subjects s " +
            "WHERE t.information.role = com.cepa.generalservice.data.constants.Role.TEACHER")
    Page<TeacherResponseInterface> getTeacherResponses(Pageable pageable);

    @Query("SELECT " +
            "t.information.id AS id, " +
            "t.information.email AS email, " +
            "t.information.fullName AS fullName, " +
            "t.information.imageURL AS url, " +
            "t.information.createDate AS createDate, " +
            "t.information.description AS description, " +
            "t.information.status AS status, " +
            "s.name AS subject, " +
            "t.information.dateOfBirth AS dateOfBirth, " +
            "t.isValidation AS isVerify " +
            "FROM Teacher t " +
            "LEFT JOIN t.subjects s " +
            "WHERE t.information.status = :status "+
            "AND t.information.role = com.cepa.generalservice.data.constants.Role.TEACHER")
    Page<TeacherResponseInterface> getTeachersByStatus(@Param("status") UserStatus status, Pageable pageable);
}

package com.cepa.generalservice.data.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cepa.generalservice.data.entities.Teacher;
import com.cepa.generalservice.data.object.interfaces.TeacherResponseForAdminInterface;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByInformationId(long id);

    @Query("SELECT " +
            "t.id AS id, " +
            "t.information.email AS email, " +
            "t.information.fullName AS fullName, " +
            "t.information.imageURL AS url, " +
            "t.createDate AS createDate, " +
            "t.information.status AS status, " +
            "s.name AS subject, " +
            "t.information.dateOfBirth AS dateOfBirth, " +
            "t.identify AS identify " +
            "FROM Teacher t " +
            "LEFT JOIN t.subjects s " +
            "WHERE (t.identify IS NOT NULL AND t.identify <> '') " +
            "AND t.isValidation = false")
    Page<TeacherResponseForAdminInterface> findTeachersForAdmin(Pageable pageable);
}

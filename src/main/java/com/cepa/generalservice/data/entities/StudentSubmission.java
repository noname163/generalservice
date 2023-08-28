package com.cepa.generalservice.data.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "StudentSubmission")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubmission {
    @Id
    @SequenceGenerator(name = "student_submission_sequence", sequenceName = "student_submission_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "student_submission_sequence")
    private long id;

    private Double score;

    private Long testId;

    private Date submitDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private UserInformation studentInformation;
}

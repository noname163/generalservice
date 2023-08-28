package com.cepa.generalservice.data.entities;

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
@Table(name = "StudentTarget")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentTarget {
    @Id
    @SequenceGenerator(name = "student_target_sequence", sequenceName = "student_target_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "student_target_sequence")
    private long id;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Target target;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private UserInformation studentInformation;
}

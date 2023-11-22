package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.constants.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Table(name = "Student_Target")
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

    private double grade;

    @Enumerated(EnumType.STRING)
    @Default
    private StateType stateType = StateType.TRUE;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "combination_id")
    private Combination combination;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private UserInformation studentInformation;

    @OneToMany(mappedBy = "studentTarget")
    private List<SubjectTarget> subjectTargets;
}

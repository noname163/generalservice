package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;

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
@Table(name = "Subject_Target")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectTarget {
    @Id
    @SequenceGenerator(name = "subject_target_sequence", sequenceName = "subject_target_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "subject_target_sequence")
    private long id;

    private long subjectId;
    
    private Integer max;

    private Integer min;

    private double grade;

    private String level;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne()
    @JoinColumn(name = "target_id")
    private StudentTarget studentTarget;
}

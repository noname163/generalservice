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
@Table(name = "Teacher")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @SequenceGenerator(name = "Teacher_sequence", sequenceName = "Teacher_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Teacher_sequence")
    private long id;

    private String description;

    private String cardNumber;
    
    private String nameOnCard;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "information_id")
    private UserInformation information;
}

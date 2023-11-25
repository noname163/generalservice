package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Boolean isValidation;

    @Column(columnDefinition = "TEXT")
    private String identify;

    private String cardNumber;

    private String publicId;

    private String mediaType;
    
    @ManyToMany
    private List<Subject> subjects;
    
    @OneToOne
    @JoinColumn(name = "information_id")
    private UserInformation information;
}

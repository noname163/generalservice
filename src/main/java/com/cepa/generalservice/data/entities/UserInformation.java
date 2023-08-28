package com.cepa.generalservice.data.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cepa.generalservice.data.constants.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User_Information")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    @Id
    @SequenceGenerator(name = "user_information_sequence", sequenceName = "user_information_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_information_sequence")
    private long id;

    private String fullName;

    private String email;

    private String password;

    private Date dateOfBirth;

    private String imageURL;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "information")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "adminInformation")
    private List<SystemTransaction> adminTransactions;

    @OneToMany(mappedBy = "teacherInformation")
    private List<SystemTransaction> teacherTransactions;

    @OneToMany(mappedBy = "studentInformation")
    private List<SystemTransaction> studentTransactions;

    @OneToMany(mappedBy = "studentInformation")
    private List<StudentSubmission> studentSubmissions;

    @OneToMany(mappedBy = "studentInformation")
    private List<StudentTarget> studentTargets;
}

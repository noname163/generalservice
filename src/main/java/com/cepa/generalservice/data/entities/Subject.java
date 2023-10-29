package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Table(name = "Subject")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @SequenceGenerator(name = "subject_sequence", sequenceName = "subject_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "subject_sequence")
    private long id;

    private String name;

    private String url;

    private String description;

    @Default
    private Boolean state = true;
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "subject")
    private List<Topic> topics;

    @ManyToMany
    private List<Teacher> teachers;

    @ManyToMany
    private List<Combination> combinations;
}

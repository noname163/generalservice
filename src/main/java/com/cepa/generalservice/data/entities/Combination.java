package com.cepa.generalservice.data.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Combination")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Combination {
    @Id
    @SequenceGenerator(name = "combination_sequence", sequenceName = "combination_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "combination_sequence")
    private long id;

    private String name;

    private String description;

    @JsonProperty
    private String subjectId;

    @OneToMany(mappedBy = "combination")
    private List<StudentTarget> studentTargets;
}
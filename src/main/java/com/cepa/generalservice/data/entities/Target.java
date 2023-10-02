package com.cepa.generalservice.data.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Target")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Target {
    @Id
    @SequenceGenerator(name = "target_sequence", sequenceName = "target_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "target_sequence")
    private long id;

    private Integer max;

    private Integer min;

    private String level;

    @ManyToOne
    @JoinColumn(name = "subjetc_id")
    private Subject subject;
}

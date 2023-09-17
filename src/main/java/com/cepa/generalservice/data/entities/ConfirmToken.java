package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "ConfirmToken")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmToken {
    @Id
    @SequenceGenerator(name = "confirm_token_sequence", sequenceName = "confirm_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "confirm_token_sequence")
    private long id;

    private UUID token;

    private LocalDateTime createAt;

    private LocalDateTime expriedAt;

    private Integer count;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInformation userInformation;
}

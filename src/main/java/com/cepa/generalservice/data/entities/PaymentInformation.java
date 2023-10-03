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
@Table(name = "Payment_Information")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {
    @Id
    @SequenceGenerator(name = "payment_information_sequence", sequenceName = "payment_information_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "payment_information_sequence")
    private long id;

    private String description;

    private String cardNumber;
    
    private String nameOnCard;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private UserInformation information;
}

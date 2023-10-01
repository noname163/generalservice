package com.cepa.generalservice.data.entities;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cepa.generalservice.data.constants.PaymentFor;
import com.cepa.generalservice.data.constants.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "System_Transaction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemTransaction {
    @Id
    @SequenceGenerator(name = "system_transaction_sequence", sequenceName = "system_transaction_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system_transaction_sequence")
    private long id;

    private LocalDateTime createDate;

    private TransactionStatus status;

    private Double amount;

    private PaymentFor paymentFor;

    private Long objectId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private UserInformation adminInformation;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private UserInformation teacherInformation;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private UserInformation studentInformation;
}

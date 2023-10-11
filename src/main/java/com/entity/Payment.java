package com.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name="payments")
public class Payment {
    @Id
    @Column(name = "payment_id", length = 100)
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @Column(name = "payment_type", length = 150, nullable = false)
    private String paymentType;

    @Column(name = "credit_card", length = 100)
    private String creditCard;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "created_on")
    private Date createdOn;
}

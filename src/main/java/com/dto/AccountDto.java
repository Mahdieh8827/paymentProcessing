package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;


/**
 * @author Mahdieh
 * On 10 Oct 2023
 */
@Getter
@Setter
public class AccountDto {
    private String paymentId;
    private Long accountId;
    private String paymentType;
    private String creditCard;
    private BigDecimal amount;
    private Date createdOn;
}

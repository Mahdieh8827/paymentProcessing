package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mahdieh
 * On 10 Oct 2023
 */
@Getter
@Setter
public class PaymentDto {
    private String payment_id;
    private String account_id;
    private String payment_type;
    private String credit_card;
    private BigDecimal amount;
    private Date created_on;
}

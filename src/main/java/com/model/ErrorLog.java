package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorLog {
    private String payment_id;
    private String error;
    private String error_description;
}

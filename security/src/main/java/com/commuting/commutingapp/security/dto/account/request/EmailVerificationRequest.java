package com.commuting.commutingapp.security.dto.account.request;

import lombok.Data;

@Data
public class EmailVerificationRequest {
    private String email;
    private String code;
}

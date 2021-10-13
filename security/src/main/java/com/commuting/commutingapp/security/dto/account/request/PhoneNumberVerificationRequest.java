package com.commuting.commutingapp.security.dto.account.request;

import lombok.Data;

@Data
public class PhoneNumberVerificationRequest {
    private String phoneNumber;
    private String code;


}

package com.commuting.commutingapp.security.dto.account.request;

import lombok.Data;

@Data
public class PhoneNumberAuthenticationRequest {
    private String phoneNumber;
}

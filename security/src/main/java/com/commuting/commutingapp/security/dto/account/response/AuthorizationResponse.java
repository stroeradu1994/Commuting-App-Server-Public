package com.commuting.commutingapp.security.dto.account.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse {
    private String accessToken;
    private String refreshToken;
}

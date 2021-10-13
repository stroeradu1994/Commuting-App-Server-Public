package com.commuting.commutingapp.security.dto.request;

import com.commuting.commutingapp.common.dto.JwtData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;

@Data
@AllArgsConstructor
public class CreateTokenRequest {
    Authentication authentication;
    JwtData jwtData;
}

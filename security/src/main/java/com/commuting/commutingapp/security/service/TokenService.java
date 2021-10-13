package com.commuting.commutingapp.security.service;

import com.commuting.commutingapp.common.dto.JwtData;
import com.commuting.commutingapp.security.dto.request.CreateTokenRequest;

public interface TokenService {
    String createAccessToken(CreateTokenRequest createTokenRequest);

    String createRefreshToken(CreateTokenRequest createTokenRequest);

    void getDataFromRefreshToken(String refreshToken);

    JwtData getJwtData(String token);
}

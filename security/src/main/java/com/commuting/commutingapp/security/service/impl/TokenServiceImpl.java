package com.commuting.commutingapp.security.service.impl;

import com.commuting.commutingapp.common.dto.JwtData;
import com.commuting.commutingapp.security.dto.request.CreateTokenRequest;
import com.commuting.commutingapp.security.security.jwt.TokenProvider;
import com.commuting.commutingapp.security.service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenProvider tokenProvider;

    @Override
    public String createAccessToken(CreateTokenRequest createTokenRequest) {
        return tokenProvider.createToken(createTokenRequest, true);
    }

    @Override
    public String createRefreshToken(CreateTokenRequest createTokenRequest) {
        return tokenProvider.createToken(createTokenRequest, false);
    }

    @Override
    public void getDataFromRefreshToken(String refreshToken) {
        Claims claims = tokenProvider.getTokenParser()
                .parseClaimsJws(refreshToken)
                .getBody();
        if (!claims.get(TokenProvider.TYPE_KEY, String.class).equals("REFRESH")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token not valid");
        }
    }

    @Override
    public JwtData getJwtData(String token) {
        return tokenProvider.getJwtData(token);
    }
}

package com.commuting.commutingapp.security.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static com.commuting.commutingapp.security.security.jwt.JWTFilter.AUTHORIZATION_HEADER;
import static com.commuting.commutingapp.security.security.jwt.JWTFilter.AUTHORIZATION_TOKEN;

public class JwtUtils {

    public static Optional<String> resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        String jwt = request.getParameter(AUTHORIZATION_TOKEN);
        return StringUtils.hasText(jwt) ? Optional.of(jwt) : Optional.empty();
    }
}

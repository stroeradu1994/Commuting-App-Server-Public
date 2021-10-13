package com.commuting.commutingapp.security.security.jwt;

import com.commuting.commutingapp.common.service.JsonConverterService;
import com.commuting.commutingapp.common.dto.JwtData;
import com.commuting.commutingapp.security.dto.request.CreateTokenRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    public static final String TYPE_KEY = "type";

    private static final long ACCESS_TOKEN_VALIDITY = 172800; // two days
    private static final long REFRESH_TOKEN_VALIDITY = 2592000; // two days

    private Key key;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    @Autowired
    JsonConverterService jsonConverterService;

    @Value("${JWT_KEY}")
    String jwtKey;

    @PostConstruct
    public void init() {
//        String secret = "aov46csLcMTnz5xCqKD4vBHF/HmnibcFeXBFJFkelfFaPBO+aspaIJQzRA76TYHERbgEoeZxseGRRq98s+VcPw==";
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * ACCESS_TOKEN_VALIDITY;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * REFRESH_TOKEN_VALIDITY;
    }

    public String createToken(CreateTokenRequest createTokenRequest, boolean isShortToken) {
        String authorities = createTokenRequest.getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (isShortToken) {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        } else {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        }

        return Jwts.builder()
                .setSubject(jsonConverterService.convertToJson(createTokenRequest.getJwtData()))
                .claim(AUTHORITIES_KEY, authorities)
                .claim(TYPE_KEY, isShortToken ? "ACCESS" : "REFRESH")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public JwtData getJwtData(String token) {
        Jwt jwt = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parse(token);
        String subject = ((DefaultClaims) jwt.getBody()).getSubject();

        return (JwtData) jsonConverterService.convertFromJson(subject, JwtData.class);
    }

    public JwtParser getTokenParser() {
        return Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = getTokenParser().parseClaimsJws(authToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {0}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {0}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {0}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {0}", e);
        }
        return false;
    }
}

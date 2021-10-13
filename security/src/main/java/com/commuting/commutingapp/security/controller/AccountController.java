package com.commuting.commutingapp.security.controller;

import com.codahale.metrics.annotation.Timed;
import com.commuting.commutingapp.security.dto.account.request.EmailAuthenticationRequest;
import com.commuting.commutingapp.security.dto.account.request.EmailVerificationRequest;
import com.commuting.commutingapp.security.dto.account.request.PhoneNumberAuthenticationRequest;
import com.commuting.commutingapp.security.dto.account.request.PhoneNumberVerificationRequest;
import com.commuting.commutingapp.security.dto.account.response.AuthorizationResponse;
import com.commuting.commutingapp.security.dto.request.AddFcmTokenRequest;
import com.commuting.commutingapp.security.dto.request.AddNameRequest;
import com.commuting.commutingapp.security.service.AccountService;
import com.commuting.commutingapp.security.security.jwt.JWTFilter;
import com.commuting.commutingapp.security.utils.JwtUtils;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @PostMapping("/email/authentication")
    ResponseEntity emailAuthentication(@Valid @RequestBody EmailAuthenticationRequest emailAuthenticationRequest) {
        accountService.emailAuthentication(emailAuthenticationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verification")
    ResponseEntity emailVerification(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) throws NotFoundException {
        return ResponseEntity.ok(accountService.emailVerification(emailVerificationRequest));
    }

    @PostMapping("/phone/authentication")
    ResponseEntity phoneNumberAuthentication(@Valid @RequestBody PhoneNumberAuthenticationRequest phoneNumberAuthenticationRequest) {
        accountService.phoneNumberAuthentication(phoneNumberAuthenticationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/phone/verification")
    ResponseEntity phoneNumberVerification(@Valid @RequestBody PhoneNumberVerificationRequest phoneNumberVerificationRequest) throws NotFoundException {
        return ResponseEntity.ok(accountService.phoneNumberVerification(phoneNumberVerificationRequest));
    }

    @PutMapping("/name")
    ResponseEntity addName(@RequestBody AddNameRequest addNameRequest) {
        accountService.addName(addNameRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/fcm")
    ResponseEntity addFcmToken(@RequestBody AddFcmTokenRequest addFcmTokenRequest) {
        accountService.addFcmToken(addFcmTokenRequest.getToken());
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    ResponseEntity getAccount() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @PostMapping("/reissue")
    @Timed
    public ResponseEntity<AuthorizationResponse> reissueTokens(ServletRequest servletRequest) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = JwtUtils.resolveToken(httpServletRequest).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token not found"));

        AuthorizationResponse authorizationResponse = accountService.reissueTokens(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + authorizationResponse.getAccessToken());
        return new ResponseEntity<>(authorizationResponse, httpHeaders, HttpStatus.OK);
    }
}

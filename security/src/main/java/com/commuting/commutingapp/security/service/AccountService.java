package com.commuting.commutingapp.security.service;


import com.commuting.commutingapp.security.dto.account.request.*;
import com.commuting.commutingapp.security.dto.account.response.AccountResponse;
import com.commuting.commutingapp.security.dto.account.response.AuthorizationResponse;
import com.commuting.commutingapp.security.dto.request.AddNameRequest;
import com.commuting.commutingapp.security.model.Account;
import javassist.NotFoundException;

public interface AccountService {

    void emailAuthentication(EmailAuthenticationRequest emailAuthenticationRequest);

    void phoneNumberAuthentication(PhoneNumberAuthenticationRequest phoneNumberAuthenticationRequest);

    AuthorizationResponse emailVerification(EmailVerificationRequest emailVerificationRequest) throws NotFoundException;

    AuthorizationResponse phoneNumberVerification(PhoneNumberVerificationRequest phoneNumberVerificationRequest) throws NotFoundException;

    AuthorizationResponse reissueTokens(String refreshToken);

    void addName(AddNameRequest addNameRequest);

    AccountResponse getAccount();

    Account getInternalAccount(String userId);

    Account getInternalAccount();

    void addFcmToken(String token);
}

package com.commuting.commutingapp.security.service.impl;

import com.commuting.commutingapp.common.dto.JwtData;
import com.commuting.commutingapp.common.utils.RandomUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.event.model.Event;
import com.commuting.commutingapp.event.model.EventType;
import com.commuting.commutingapp.event.service.EventService;
import com.commuting.commutingapp.mail.dto.RecipientInformation;
import com.commuting.commutingapp.mail.service.MailService;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.security.dto.account.request.*;
import com.commuting.commutingapp.security.dto.account.response.AccountResponse;
import com.commuting.commutingapp.security.dto.account.response.AuthorizationResponse;
import com.commuting.commutingapp.security.dto.request.AddNameRequest;
import com.commuting.commutingapp.security.dto.request.CreateTokenRequest;
import com.commuting.commutingapp.security.model.Account;
import com.commuting.commutingapp.security.repo.AccountRepo;
import com.commuting.commutingapp.security.security.AuthoritiesConstants;
import com.commuting.commutingapp.security.service.AccountService;
import com.commuting.commutingapp.security.service.TokenService;
import com.commuting.commutingapp.sms.service.SmsService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EventService eventService;

    @Override
    public void emailAuthentication(EmailAuthenticationRequest emailAuthenticationRequest) {
        String email = emailAuthenticationRequest.getEmail().toLowerCase();
        Optional<Account> existingAccount = accountRepo.findByEmail(email);

        Account account;
        if (existingAccount.isPresent()) {
            account = existingAccount.get();
            account.setEmailVerificationKey(RandomUtils.generateCode());
            account.setEmailVerificationDate(Instant.now());
            account = accountRepo.save(account);
        } else {
            Account newAccount = new Account();
            newAccount.setEmail(email);
            newAccount.setEmailVerified(false);
            newAccount.setPhoneNumberVerified(false);
            newAccount.setEmailVerificationKey(RandomUtils.generateCode());
            newAccount.setEmailVerificationDate(Instant.now());
            newAccount.setAuthorities(List.of(AuthoritiesConstants.USER));

            account = accountRepo.save(newAccount);

            log.debug("Created Account for User: {}", account);

            Profile profile = new Profile();
            profile.setId(account.getId());
            profile.setEmail(email);

            profileService.createUpdateProfile(profile);
        }

        eventService.logEvent(new Event(account.getId(), EventType.EMAIL_AUTHENTICATION));

        mailService.sendVerificationEmail(new RecipientInformation(email, "", account.getEmailVerificationKey()));
    }

    @Override
    public void phoneNumberAuthentication(PhoneNumberAuthenticationRequest phoneNumberAuthenticationRequest) {
        Account account = accountRepo.findByPhoneNumber(phoneNumberAuthenticationRequest.getPhoneNumber())
                .orElseGet(() -> {
                    String userId = SecurityUtils.getUserId();
                    return accountRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
                });

        account.setPhoneNumber(phoneNumberAuthenticationRequest.getPhoneNumber());

        accountRepo.save(account);

        eventService.logEvent(new Event(account.getId(), EventType.EMAIL_AUTHENTICATION));

        smsService.sendPhoneNumberVerificationSms(phoneNumberAuthenticationRequest.getPhoneNumber());
    }

    @Override
    public AuthorizationResponse emailVerification(EmailVerificationRequest emailVerificationRequest) throws NotFoundException {
        Optional<Account> existingAccount = accountRepo.findByEmail(emailVerificationRequest.getEmail().toLowerCase());
        if (existingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account with this email was found");

        Account account = existingAccount.get();

        if (account.getEmailVerificationDate().isAfter(Instant.now().plus(1, ChronoUnit.DAYS)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is expired");

        if (!account.getEmailVerificationKey().equals(emailVerificationRequest.getCode()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code does not match");

        List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(emailVerificationRequest.getEmail().toLowerCase(), null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        account.setEmailVerificationDate(null);
        account.setEmailVerificationKey(null);
        account.setEmailVerified(true);

        accountRepo.save(account);

        JwtData jwtData = new JwtData(account.getId(), account.getFirstName(), account.getLastName(), account.getEmail());

        return getAccessRefreshTokens(authentication, jwtData);
    }

    @Override
    public AuthorizationResponse phoneNumberVerification(PhoneNumberVerificationRequest phoneNumberVerificationRequest) throws NotFoundException {
        Optional<Account> existingAccount = accountRepo.findByPhoneNumber(phoneNumberVerificationRequest.getPhoneNumber().toLowerCase());
        if (existingAccount.isEmpty()) {
            throw new NotFoundException("");
        } else {
            Account account = existingAccount.get();
            if (smsService.isPhoneNumberVerified(phoneNumberVerificationRequest.getPhoneNumber(), phoneNumberVerificationRequest.getCode())) {

                List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(account.getEmail().toLowerCase(), null, grantedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                account.setPhoneNumberVerificationKey(null);
                account.setPhoneNumberVerificationDate(null);
                account.setPhoneNumberVerified(true);

                accountRepo.save(account);

                Profile profile = profileService.getById(account.getId());
                profile.setPhone(phoneNumberVerificationRequest.getPhoneNumber());
                profileService.createUpdateProfile(profile);

                JwtData jwtData = new JwtData(account.getId(), account.getFirstName(), account.getLastName(), account.getEmail());

                return getAccessRefreshTokens(authentication, jwtData);
            } else {
                throw new NotFoundException("");
            }
        }
    }

    @Override
    public AuthorizationResponse reissueTokens(String refreshToken) {
        tokenService.getDataFromRefreshToken(refreshToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtData jwtData = tokenService.getJwtData(refreshToken);
        return getAccessRefreshTokens(authentication, jwtData);
    }

    @Override
    public void addName(AddNameRequest addNameRequest) {
        String userId = SecurityUtils.getUserId();
        Account account = accountRepo.findById(userId).orElseThrow();

        account.setFirstName(addNameRequest.getFirstName());
        account.setLastName(addNameRequest.getLastName());

        accountRepo.save(account);

        profileService.addName(new com.commuting.commutingapp.profile.dto.request.AddNameRequest(userId, addNameRequest.getFirstName(), addNameRequest.getLastName()));
    }

    @Override
    public AccountResponse getAccount() {
        String userId = SecurityUtils.getUserId();
        Account account = accountRepo.findById(userId).orElseThrow();
        return new AccountResponse(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getPhoneNumber(),
                account.isPhoneNumberVerified()
        );
    }

    @Override
    public Account getInternalAccount(String userId) {
        return accountRepo.findById(userId).orElseThrow();
    }

    @Override
    public Account getInternalAccount() {
        String userId = SecurityUtils.getUserId();
        return getInternalAccount(userId);
    }

    @Override
    public void addFcmToken(String token) {
        Account account = getInternalAccount();
        account.setFcmToken(token);
        accountRepo.save(account);
    }

    private AuthorizationResponse getAccessRefreshTokens(Authentication authentication, JwtData jwtData) {
        CreateTokenRequest createTokenRequest = new CreateTokenRequest(authentication, jwtData);

        String accessToken = tokenService.createAccessToken(createTokenRequest);
        String refreshToken = tokenService.createRefreshToken(createTokenRequest);
        return new AuthorizationResponse(accessToken, refreshToken);
    }

    private static boolean checkPasswordLength(String password) {
        return !ObjectUtils.isEmpty(password) &&
                password.length() >= RegisterUserRequest.PASSWORD_MIN_LENGTH &&
                password.length() <= RegisterUserRequest.PASSWORD_MAX_LENGTH;
    }
}

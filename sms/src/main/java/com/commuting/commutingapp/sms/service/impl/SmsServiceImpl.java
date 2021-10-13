package com.commuting.commutingapp.sms.service.impl;

import com.commuting.commutingapp.sms.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@org.springframework.stereotype.Service
public class SmsServiceImpl implements SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String SERVICE_ID = "";

    @PostConstruct
    void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void sendPhoneNumberVerificationSms(String phoneNumber) {
//        Verification.creator(SERVICE_ID, phoneNumber, "sms").create();
    }

    @Override
    public boolean isPhoneNumberVerified(String phoneNumber, String code) {
//        VerificationCheck verificationCheck = VerificationCheck.creator(SERVICE_ID, code).setTo(phoneNumber).create();
//        return verificationCheck.getStatus().equals("approved");
        return true;
    }
}

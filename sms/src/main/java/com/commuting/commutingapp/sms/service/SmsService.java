package com.commuting.commutingapp.sms.service;

public interface SmsService {
    void sendPhoneNumberVerificationSms(String phoneNumber);
    boolean isPhoneNumberVerified(String phoneNumber, String code);
}

package com.commuting.commutingapp.mail.service;

import com.commuting.commutingapp.mail.dto.RecipientInformation;

public interface MailService {

    void sendVerificationEmail(RecipientInformation recipientInformation);
}

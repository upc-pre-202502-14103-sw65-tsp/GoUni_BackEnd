package com.studentconnect.gouni.platform.chat.domain.services;

import com.studentconnect.gouni.platform.chat.domain.model.entities.EmailDetails;

public interface EmailService {
    String sendEmail(EmailDetails details);
    String sendEmailWithAttachment(EmailDetails details);
}

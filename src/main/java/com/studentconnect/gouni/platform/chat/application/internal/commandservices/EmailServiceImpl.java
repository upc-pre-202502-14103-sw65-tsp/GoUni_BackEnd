package com.studentconnect.gouni.platform.chat.application.internal.commandservices;

import com.studentconnect.gouni.platform.chat.domain.model.entities.EmailDetails;
import com.studentconnect.gouni.platform.chat.domain.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired private JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String sender;

    @Override
    public String sendEmail(EmailDetails details) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(details.getRecipient());
            message.setText(details.getMsgBody());
            message.setSubject(details.getSubject());

            mailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            return "Error while sending email: " + e.getMessage();
        }
    }

    @Override
    public String sendEmailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setText(details.getMsgBody());
            helper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);
            return "Email sent successfully";
        } catch (MessagingException e) {
            return  "Error while sending email: " + e.getMessage();
        }
    }
}

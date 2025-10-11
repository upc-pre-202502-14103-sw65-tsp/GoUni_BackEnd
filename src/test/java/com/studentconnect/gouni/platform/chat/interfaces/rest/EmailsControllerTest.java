package com.studentconnect.gouni.platform.chat.interfaces.rest;

import com.studentconnect.gouni.platform.chat.domain.model.entities.EmailDetails;
import com.studentconnect.gouni.platform.chat.domain.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("EmailsController Tests")
class EmailsControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailsController emailsController;

    private EmailDetails validEmailDetails;
    private EmailDetails emailWithAttachment;

    @BeforeEach
    void setUp() {
        validEmailDetails = new EmailDetails();
        validEmailDetails.setRecipient("test@upc.edu.pe");
        validEmailDetails.setSubject("Test Email Subject");
        validEmailDetails.setMsgBody("This is a test email body");
        validEmailDetails.setAttachment(null);

        emailWithAttachment = new EmailDetails();
        emailWithAttachment.setRecipient("test@upc.edu.pe");
        emailWithAttachment.setSubject("Test Email with Attachment");
        emailWithAttachment.setMsgBody("This is a test email with attachment");
        emailWithAttachment.setAttachment("/path/to/attachment.pdf");
    }

    @Test
    @DisplayName("Should send email successfully")
    void shouldSendEmailSuccessfully() {
        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMail(validEmailDetails);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should handle email sending failure")
    void shouldHandleEmailSendingFailure() {
        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Error while sending email: SMTP connection failed");

        String result = emailsController.sendMail(validEmailDetails);

        assertNotNull(result);
        assertTrue(result.contains("Error while sending email"));
        assertTrue(result.contains("SMTP connection failed"));

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should send email with different recipients")
    void shouldSendEmailWithDifferentRecipients() {
        EmailDetails differentRecipient = new EmailDetails();
        differentRecipient.setRecipient("admin@upc.edu.pe");
        differentRecipient.setSubject("Admin Notification");
        differentRecipient.setMsgBody("This is an admin notification");
        differentRecipient.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMail(differentRecipient);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should send email with empty subject")
    void shouldSendEmailWithEmptySubject() {
        EmailDetails emptySubjectEmail = new EmailDetails();
        emptySubjectEmail.setRecipient("test@upc.edu.pe");
        emptySubjectEmail.setSubject("");
        emptySubjectEmail.setMsgBody("Email with empty subject");
        emptySubjectEmail.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMail(emptySubjectEmail);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should send email with long message body")
    void shouldSendEmailWithLongMessageBody() {
        EmailDetails longMessageEmail = new EmailDetails();
        longMessageEmail.setRecipient("test@upc.edu.pe");
        longMessageEmail.setSubject("Long Message Test");
        longMessageEmail.setMsgBody("This is a very long message body that contains multiple lines of text. " +
                "It should test the system's ability to handle longer email content. " +
                "The message continues with more details and information that might be relevant to the recipient.");
        longMessageEmail.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMail(longMessageEmail);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should send email with attachment successfully")
    void shouldSendEmailWithAttachmentSuccessfully() {
        when(emailService.sendEmailWithAttachment(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMailWithAttachment(emailWithAttachment);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmailWithAttachment(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should handle email with attachment failure")
    void shouldHandleEmailWithAttachmentFailure() {
        when(emailService.sendEmailWithAttachment(any(EmailDetails.class)))
                .thenReturn("Error while sending email: File not found");

        String result = emailsController.sendMailWithAttachment(emailWithAttachment);

        assertNotNull(result);
        assertTrue(result.contains("Error while sending email"));
        assertTrue(result.contains("File not found"));

        verify(emailService, times(1)).sendEmailWithAttachment(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should handle email with invalid attachment path")
    void shouldHandleEmailWithInvalidAttachmentPath() {
        EmailDetails invalidAttachmentEmail = new EmailDetails();
        invalidAttachmentEmail.setRecipient("test@upc.edu.pe");
        invalidAttachmentEmail.setSubject("Invalid Attachment Test");
        invalidAttachmentEmail.setMsgBody("This email has an invalid attachment path");
        invalidAttachmentEmail.setAttachment("/invalid/path/nonexistent.pdf");

        when(emailService.sendEmailWithAttachment(any(EmailDetails.class)))
                .thenReturn("Error while sending email: Attachment file not found");

        String result = emailsController.sendMailWithAttachment(invalidAttachmentEmail);

        assertNotNull(result);
        assertTrue(result.contains("Error while sending email"));
        assertTrue(result.contains("Attachment file not found"));

        verify(emailService, times(1)).sendEmailWithAttachment(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should handle email with null recipient")
    void shouldHandleEmailWithNullRecipient() {
        EmailDetails nullRecipientEmail = new EmailDetails();
        nullRecipientEmail.setRecipient(null);
        nullRecipientEmail.setSubject("Test Subject");
        nullRecipientEmail.setMsgBody("Test message");
        nullRecipientEmail.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Error while sending email: Recipient cannot be null");

        String result = emailsController.sendMail(nullRecipientEmail);

        assertNotNull(result);
        assertTrue(result.contains("Error while sending email"));
        assertTrue(result.contains("Recipient cannot be null"));

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should handle email with null message body")
    void shouldHandleEmailWithNullMessageBody() {
        EmailDetails nullBodyEmail = new EmailDetails();
        nullBodyEmail.setRecipient("test@upc.edu.pe");
        nullBodyEmail.setSubject("Test Subject");
        nullBodyEmail.setMsgBody(null);
        nullBodyEmail.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Error while sending email: Message body cannot be null");

        String result = emailsController.sendMail(nullBodyEmail);

        assertNotNull(result);
        assertTrue(result.contains("Error while sending email"));
        assertTrue(result.contains("Message body cannot be null"));

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    @DisplayName("Should send email with special characters in subject")
    void shouldSendEmailWithSpecialCharactersInSubject() {
        EmailDetails specialCharsEmail = new EmailDetails();
        specialCharsEmail.setRecipient("test@upc.edu.pe");
        specialCharsEmail.setSubject("Test Email with Special Characters: áéíóú ñ @#$%");
        specialCharsEmail.setMsgBody("This email contains special characters in the subject");
        specialCharsEmail.setAttachment(null);

        when(emailService.sendEmail(any(EmailDetails.class)))
                .thenReturn("Email sent successfully");

        String result = emailsController.sendMail(specialCharsEmail);

        assertNotNull(result);
        assertEquals("Email sent successfully", result);

        verify(emailService, times(1)).sendEmail(any(EmailDetails.class));
    }
}

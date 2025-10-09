package com.studentconnect.gouni.platform.chat.interfaces.rest;

import com.studentconnect.gouni.platform.chat.domain.model.entities.EmailDetails;
import com.studentconnect.gouni.platform.chat.domain.services.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/emails", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Emails", description = "Emails Management Endpoint")
public class EmailsController {
    @Autowired private EmailService emailService;

    @PostMapping("/send")
    public String sendMail(@RequestBody EmailDetails details) {
        return emailService.sendEmail(details);
    }

    @PostMapping("/send-attachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        return emailService.sendEmailWithAttachment(details);
    }
}

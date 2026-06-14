package com.aston.notificationservice.controller;

import com.aston.notificationservice.dto.EmailRequest;
import com.aston.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(
                request.to(),
                request.subject(),
                request.text()
        );
    }
}

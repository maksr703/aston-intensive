package com.aston.notificationservice.dto;

public record EmailRequest(
        String to,
        String subject,
        String text
) {}

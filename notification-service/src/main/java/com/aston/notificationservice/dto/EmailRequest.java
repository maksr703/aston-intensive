package com.aston.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank
        String to,

        @NotBlank
        String subject,

        @NotBlank
        String text
) {}

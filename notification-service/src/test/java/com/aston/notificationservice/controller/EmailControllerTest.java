package com.aston.notificationservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EmailControllerTest {

    @Container
    static GenericContainer mailhog =
            new GenericContainer("mailhog/mailhog:latest")
                    .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> mailhog.getHost());
        registry.add("spring.mail.port", () -> mailhog.getMappedPort(1025));
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    void testSendEmail() throws Exception {

        String request = "{"
                + "\"to\":\"test@mail.com\","
                + "\"subject\":\"Hello\","
                + "\"text\":\"World\""
                + "}";

        mockMvc.perform(post("/api/v1/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void testSendEmailInvalidRequest() throws Exception {

        String request = "{"
                + "\"to\":\"\","
                + "\"subject\":\"\","
                + "\"text\":\"\""
                + "}";

        mockMvc.perform(post("/api/v1/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }
}

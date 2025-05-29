package ru.savin.webhook_processor_4_9.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebhookAuthenticationServiceTest {

    @Autowired
    private  WebhookAuthenticationService authenticationService;

    @Test
    void authenticate() {

        var auth = "FakePay:1234";
        var authEncode = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        var authHeader =  "Basic " + authEncode;

        var result = authenticationService.authenticate(authHeader);
        assertNotNull(result);
        assertEquals("FakePay", result.get());

    }
}
package ru.savin.webhook_processor_4_9;

import org.springframework.boot.SpringApplication;

public class TestWebhookProcessor49Application {

    public static void main(String[] args) {
        SpringApplication.from(WebhookProcessorApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

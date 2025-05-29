package ru.savin.webhook_processor_4_9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WebhookProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookProcessorApplication.class, args);
    }

}

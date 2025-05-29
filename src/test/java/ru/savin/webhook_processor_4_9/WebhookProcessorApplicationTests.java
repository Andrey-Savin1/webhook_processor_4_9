package ru.savin.webhook_processor_4_9;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class WebhookProcessorApplicationTests {

    @Test
    void contextLoads() {
    }

}

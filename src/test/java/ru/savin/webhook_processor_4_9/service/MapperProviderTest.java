package ru.savin.webhook_processor_4_9.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.savin.webhook_processor_4_9.mapper.FakeProviderMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MapperProviderTest {

    @Autowired
    private WebhookService webhookService;

    @Test
    void getMapperForProviderTest() {
        var result = webhookService.getMapperForProvider("FakePay");

        assertNotNull(result);
        assertSame(FakeProviderMapper.class, result.getClass());
    }

    @Test
    void getMapperForProviderTestException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> webhookService.getMapperForProvider("NoPainNoGain")
        );

        assertNotNull(exception);
        assertEquals("Unsupported provider: NoPainNoGain", exception.getMessage());
    }
}

package ru.savin.webhook_processor_4_9.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.savin.webhook_processor_4_9.config.ObjectMapperConfig;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;
import ru.savin.webhook_processor_4_9.repository.PaymentProviderCallbackRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
class WebHookControllerTest {

    private final ObjectMapperConfig config;
    private final WebHookController webHookController;
    private final PaymentProviderCallbackRepository paymentProviderCallbackRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        // Используем специальный URL для Testcontainers
        String jdbcUrl = "jdbc:tc:postgresql:15:///testdb?TC_TMPFS=/testtmpfs:rw";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:/db/migration-test");
    }

    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName.parse(
            "mockserver/mockserver:5.15.0"));

    @DynamicPropertySource
    static void overrideFeignClientUrl(DynamicPropertyRegistry registry) {
        // Заменяем URL Feign Client на URL MockServer
        registry.add("app.http.client.transaction-service", mockServerContainer::getEndpoint);
    }

    @Test
    void receiveWebhookTest() throws JsonProcessingException {
        var objectMapper = config.objectMapper();
        log.info("TransactionService URL: {}", mockServerContainer.getEndpoint());

        var auth = "FakePay:1234";
        var authEncode = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        var authHeader = "Basic " + authEncode;

        ProcessDto fakeProviderDto = new ProcessDto("1234", "SUCCESS", "top_up", "USD", "CARD");
        String requestBody = config.objectMapper().writeValueAsString(fakeProviderDto);

        // Настройка MockServer для имитации ответа
        MockServerClient mockServerClient = new MockServerClient(
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()
        );

        mockServerClient
                .when(
                        org.mockserver.model.HttpRequest.request()
                                .withMethod(POST.name())
                                .withPath("/api/v1/transactions/process")
                                .withBody(requestBody)
                )
                .respond(
                        org.mockserver.model.HttpResponse.response()
                                .withStatusCode(200)
                                //.withBody(responseBody)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                );

        var res = webHookController.receiveWebhook(authHeader, requestBody);
        var transaction = paymentProviderCallbackRepository.findByProviderTransactionUid(fakeProviderDto.getTransactionId());

        assertThat(Objects.requireNonNull(transaction.getProviderTransactionUid())).isEqualTo("1234");
        assertThat(Objects.requireNonNull(transaction.getTransactionStatus())).isEqualTo("SUCCESS");
        assertThat(Objects.requireNonNull(transaction.getExternalServiceSendStatus())).isEqualTo("SUCCESS");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void receiveWebhookTestFailed() throws JsonProcessingException {

        log.info("TransactionService URL: {}", mockServerContainer.getEndpoint());

        var auth = "FakePay:1234";
        var authEncode = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        var authHeader = "Basic " + authEncode;

        ProcessDto fakeProviderDto = new ProcessDto("12345", "SUCCESS", "top_up", "USD", "CARD");
        String requestBody = config.objectMapper().writeValueAsString(fakeProviderDto);

        // Настройка MockServer для имитации ответа
        MockServerClient mockServerClient = new MockServerClient(
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()
        );

        mockServerClient
                .when(
                        org.mockserver.model.HttpRequest.request()
                                .withMethod(POST.name())
                                .withPath("/api/v1/transactions/process")
                                .withBody(requestBody)
                )
                .respond(
                        org.mockserver.model.HttpResponse.response()
                                .withStatusCode(404)
                                //.withBody(responseBody)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                );

        var res = webHookController.receiveWebhook(authHeader, requestBody);
        var transaction = paymentProviderCallbackRepository.findByProviderTransactionUid(fakeProviderDto.getTransactionId());

        assertThat(Objects.requireNonNull(transaction.getProviderTransactionUid())).isEqualTo("12345");
        assertThat(Objects.requireNonNull(transaction.getTransactionStatus())).isEqualTo("SUCCESS");
        assertThat(Objects.requireNonNull(transaction.getExternalServiceSendStatus())).isEqualTo("FAILED");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }
}
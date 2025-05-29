package ru.savin.webhook_processor_4_9.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.savin.webhook_processor_4_9.model.ProviderAuthData;
import ru.savin.webhook_processor_4_9.repository.ProviderAuthDataRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;


/**
 * Сервис для аутентификации провайдеров по заголовку Basic Auth.
 * <p>
 * Предоставляет метод проверки подлинности провайдера на основе заголовка Authorization:
 * извлекает имя и пароль, сверяет их с данными в БД.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookAuthenticationService {

    private final ProviderAuthDataRepository providerAuthDataRepository;


    /**
     * Аутентифицирует провайдера по заголовку Basic Auth.
     * <p>
     * Алгоритм:
     * Проверяет формат заголовка (начинается с "Basic ").
     * Декодирует Base64-строку.
     * Разбивает строку на имя пользователя и пароль.
     * Ищет провайдера в БД по имени.
     * Сравнивает пароли безопасным образом (с использованием MessageDigest).
     *
     * @param authHeader заголовок авторизации (например: "Basic dXNlcjpwYXNzd29yZA==")
     * @return {@link Optional} с именем провайдера, если аутентификация успешна, иначе — пустой Optional
     */
    public Optional<String> authenticate(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return Optional.empty();
        }
        String base64Credentials = authHeader.substring(6); // Убираем "Basic "
        String decoded;

        try {
            decoded = new String(Base64.getDecoder().decode(base64Credentials));
        } catch (IllegalArgumentException e) {
            // Некорректный Base64
            return Optional.empty();
        }

        String[] parts = decoded.split(":", 2); // Разделение на 2 части
        if (parts.length != 2) {
            return Optional.empty();
        }

        String providerName = parts[0];
        String password = parts[1];

        return providerAuthDataRepository.findByProviderName(providerName)
                .filter(provider -> MessageDigest.isEqual(
                        password.getBytes(StandardCharsets.UTF_8),
                        provider.getProviderPassword().getBytes(StandardCharsets.UTF_8)))
                .map(ProviderAuthData::getProviderName);

    }
}

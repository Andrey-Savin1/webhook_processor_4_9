package ru.savin.webhook_processor_4_9.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "provider_auth_datas")
public class ProviderAuthData {

    @Id
    private Integer id;

    @NotNull
    @Column(name = "provider_name")
    private String providerName;

    @NotNull
    @Column(name = "provider_password")
    private String providerPassword;

}
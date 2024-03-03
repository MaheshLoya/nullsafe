package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.PaymentGateway} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGatewayDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean active;

    @NotNull
    @Size(max = 250)
    private String title;

    @NotNull
    @Size(max = 65535)
    private String keyId;

    @NotNull
    @Size(max = 65535)
    private String secretId;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentGatewayDTO)) {
            return false;
        }

        PaymentGatewayDTO paymentGatewayDTO = (PaymentGatewayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentGatewayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGatewayDTO{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", title='" + getTitle() + "'" +
            ", keyId='" + getKeyId() + "'" +
            ", secretId='" + getSecretId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

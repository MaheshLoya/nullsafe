package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.PasswordResets} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasswordResetsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String token;

    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordResetsDTO)) {
            return false;
        }

        PasswordResetsDTO passwordResetsDTO = (PasswordResetsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, passwordResetsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasswordResetsDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", token='" + getToken() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

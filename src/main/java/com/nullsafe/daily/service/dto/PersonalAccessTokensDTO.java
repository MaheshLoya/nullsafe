package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.PersonalAccessTokens} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalAccessTokensDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tokenableType;

    @NotNull
    private Long tokenableId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 64)
    private String token;

    @Size(max = 65535)
    private String abilities;

    private Instant lastUsedAt;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenableType() {
        return tokenableType;
    }

    public void setTokenableType(String tokenableType) {
        this.tokenableType = tokenableType;
    }

    public Long getTokenableId() {
        return tokenableId;
    }

    public void setTokenableId(Long tokenableId) {
        this.tokenableId = tokenableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
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
        if (!(o instanceof PersonalAccessTokensDTO)) {
            return false;
        }

        PersonalAccessTokensDTO personalAccessTokensDTO = (PersonalAccessTokensDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personalAccessTokensDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalAccessTokensDTO{" +
            "id=" + getId() +
            ", tokenableType='" + getTokenableType() + "'" +
            ", tokenableId=" + getTokenableId() +
            ", name='" + getName() + "'" +
            ", token='" + getToken() + "'" +
            ", abilities='" + getAbilities() + "'" +
            ", lastUsedAt='" + getLastUsedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

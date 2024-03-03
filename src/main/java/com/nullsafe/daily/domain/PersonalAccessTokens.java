package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PersonalAccessTokens.
 */
@Entity
@Table(name = "personal_access_tokens")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalAccessTokens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tokenable_type", length = 255, nullable = false)
    private String tokenableType;

    @NotNull
    @Column(name = "tokenable_id", nullable = false)
    private Long tokenableId;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 64)
    @Column(name = "token", length = 64, nullable = false, unique = true)
    private String token;

    @Size(max = 65535)
    @Column(name = "abilities", length = 65535)
    private String abilities;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalAccessTokens id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenableType() {
        return this.tokenableType;
    }

    public PersonalAccessTokens tokenableType(String tokenableType) {
        this.setTokenableType(tokenableType);
        return this;
    }

    public void setTokenableType(String tokenableType) {
        this.tokenableType = tokenableType;
    }

    public Long getTokenableId() {
        return this.tokenableId;
    }

    public PersonalAccessTokens tokenableId(Long tokenableId) {
        this.setTokenableId(tokenableId);
        return this;
    }

    public void setTokenableId(Long tokenableId) {
        this.tokenableId = tokenableId;
    }

    public String getName() {
        return this.name;
    }

    public PersonalAccessTokens name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return this.token;
    }

    public PersonalAccessTokens token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAbilities() {
        return this.abilities;
    }

    public PersonalAccessTokens abilities(String abilities) {
        this.setAbilities(abilities);
        return this;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public Instant getLastUsedAt() {
        return this.lastUsedAt;
    }

    public PersonalAccessTokens lastUsedAt(Instant lastUsedAt) {
        this.setLastUsedAt(lastUsedAt);
        return this;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public PersonalAccessTokens createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public PersonalAccessTokens updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalAccessTokens)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonalAccessTokens) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalAccessTokens{" +
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

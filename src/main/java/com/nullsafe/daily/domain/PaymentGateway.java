package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PaymentGateway.
 */
@Entity
@Table(name = "payment_gateway")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGateway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Size(max = 250)
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @NotNull
    @Size(max = 65535)
    @Column(name = "key_id", length = 65535, nullable = false)
    private String keyId;

    @NotNull
    @Size(max = 65535)
    @Column(name = "secret_id", length = 65535, nullable = false)
    private String secretId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentGateway id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public PaymentGateway active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return this.title;
    }

    public PaymentGateway title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public PaymentGateway keyId(String keyId) {
        this.setKeyId(keyId);
        return this;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSecretId() {
        return this.secretId;
    }

    public PaymentGateway secretId(String secretId) {
        this.setSecretId(secretId);
        return this;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public PaymentGateway createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public PaymentGateway updatedAt(Instant updatedAt) {
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
        if (!(o instanceof PaymentGateway)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentGateway) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGateway{" +
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

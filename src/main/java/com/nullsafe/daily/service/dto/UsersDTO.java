package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Users} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsersDTO implements Serializable {

    private Long id;

    private Double walletAmount;

    @Size(max = 255)
    private String email;

    @Size(max = 250)
    private String phone;

    private Instant emailVerifiedAt;

    @Size(max = 255)
    private String password;

    @Size(max = 100)
    private String rememberToken;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    @Size(max = 250)
    private String name;

    @Size(max = 65535)
    private String fcm;

    @NotNull
    private Integer subscriptionAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Instant emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public Integer getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Integer subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsersDTO)) {
            return false;
        }

        UsersDTO usersDTO = (UsersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersDTO{" +
            "id=" + getId() +
            ", walletAmount=" + getWalletAmount() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", emailVerifiedAt='" + getEmailVerifiedAt() + "'" +
            ", password='" + getPassword() + "'" +
            ", rememberToken='" + getRememberToken() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", name='" + getName() + "'" +
            ", fcm='" + getFcm() + "'" +
            ", subscriptionAmount=" + getSubscriptionAmount() +
            "}";
    }
}

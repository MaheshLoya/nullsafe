package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.SubscriptionRenewal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionRenewalDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer userId;

    @NotNull
    private Long orderId;

    private Long transactionId;

    @NotNull
    private LocalDate renewalDate;

    @NotNull
    private Float paidRenewalAmount;

    /**
     * 0-pending,1-active,2-expired
     */
    @NotNull
    @Schema(description = "0-pending,1-active,2-expired", required = true)
    private Boolean status;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Float getPaidRenewalAmount() {
        return paidRenewalAmount;
    }

    public void setPaidRenewalAmount(Float paidRenewalAmount) {
        this.paidRenewalAmount = paidRenewalAmount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        if (!(o instanceof SubscriptionRenewalDTO)) {
            return false;
        }

        SubscriptionRenewalDTO subscriptionRenewalDTO = (SubscriptionRenewalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionRenewalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionRenewalDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", orderId=" + getOrderId() +
            ", transactionId=" + getTransactionId() +
            ", renewalDate='" + getRenewalDate() + "'" +
            ", paidRenewalAmount=" + getPaidRenewalAmount() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

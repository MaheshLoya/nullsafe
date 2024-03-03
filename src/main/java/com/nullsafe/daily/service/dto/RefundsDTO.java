package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Refunds} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefundsDTO implements Serializable {

    @NotNull
    private Integer id;

    private Integer orderId;

    @Size(max = 255)
    private String transactionId;

    @Size(max = 255)
    private String razorpayRefundId;

    @Size(max = 255)
    private String razorpayPaymentId;

    private BigDecimal amount;

    @Size(max = 3)
    private String currency;

    @Size(max = 50)
    private String status;

    @Size(max = 255)
    private String createdBy;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRazorpayRefundId() {
        return razorpayRefundId;
    }

    public void setRazorpayRefundId(String razorpayRefundId) {
        this.razorpayRefundId = razorpayRefundId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        if (!(o instanceof RefundsDTO)) {
            return false;
        }

        RefundsDTO refundsDTO = (RefundsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refundsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundsDTO{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", transactionId='" + getTransactionId() + "'" +
            ", razorpayRefundId='" + getRazorpayRefundId() + "'" +
            ", razorpayPaymentId='" + getRazorpayPaymentId() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

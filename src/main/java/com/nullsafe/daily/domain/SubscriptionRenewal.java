package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A SubscriptionRenewal.
 */
@Entity
@Table(name = "subscription_renewal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionRenewal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "transaction_id")
    private Long transactionId;

    @NotNull
    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @NotNull
    @Column(name = "paid_renewal_amount", nullable = false)
    private Float paidRenewalAmount;

    /**
     * 0-pending,1-active,2-expired
     */
    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionRenewal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public SubscriptionRenewal userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public SubscriptionRenewal orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public SubscriptionRenewal transactionId(Long transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getRenewalDate() {
        return this.renewalDate;
    }

    public SubscriptionRenewal renewalDate(LocalDate renewalDate) {
        this.setRenewalDate(renewalDate);
        return this;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Float getPaidRenewalAmount() {
        return this.paidRenewalAmount;
    }

    public SubscriptionRenewal paidRenewalAmount(Float paidRenewalAmount) {
        this.setPaidRenewalAmount(paidRenewalAmount);
        return this;
    }

    public void setPaidRenewalAmount(Float paidRenewalAmount) {
        this.paidRenewalAmount = paidRenewalAmount;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public SubscriptionRenewal status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public SubscriptionRenewal startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public SubscriptionRenewal endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SubscriptionRenewal createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SubscriptionRenewal updatedAt(Instant updatedAt) {
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
        if (!(o instanceof SubscriptionRenewal)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionRenewal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionRenewal{" +
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

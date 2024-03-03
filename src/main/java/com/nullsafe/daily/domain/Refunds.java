package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Refunds.
 */
@Entity
@Table(name = "refunds")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Refunds implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Size(max = 255)
    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Size(max = 255)
    @Column(name = "razorpay_refund_id", length = 255)
    private String razorpayRefundId;

    @Size(max = 255)
    @Column(name = "razorpay_payment_id", length = 255)
    private String razorpayPaymentId;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Size(max = 3)
    @Column(name = "currency", length = 3)
    private String currency;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 255)
    @Column(name = "created_by", length = 255)
    private String createdBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public Refunds id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public Refunds orderId(Integer orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Refunds transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRazorpayRefundId() {
        return this.razorpayRefundId;
    }

    public Refunds razorpayRefundId(String razorpayRefundId) {
        this.setRazorpayRefundId(razorpayRefundId);
        return this;
    }

    public void setRazorpayRefundId(String razorpayRefundId) {
        this.razorpayRefundId = razorpayRefundId;
    }

    public String getRazorpayPaymentId() {
        return this.razorpayPaymentId;
    }

    public Refunds razorpayPaymentId(String razorpayPaymentId) {
        this.setRazorpayPaymentId(razorpayPaymentId);
        return this;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Refunds amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Refunds currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return this.status;
    }

    public Refunds status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Refunds createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Refunds createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Refunds updatedAt(Instant updatedAt) {
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
        if (!(o instanceof Refunds)) {
            return false;
        }
        return getId() != null && getId().equals(((Refunds) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Refunds{" +
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

package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Refunds} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.RefundsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /refunds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefundsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private IntegerFilter id;

    private IntegerFilter orderId;

    private StringFilter transactionId;

    private StringFilter razorpayRefundId;

    private StringFilter razorpayPaymentId;

    private BigDecimalFilter amount;

    private StringFilter currency;

    private StringFilter status;

    private StringFilter createdBy;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public RefundsCriteria() {}

    public RefundsCriteria(RefundsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.razorpayRefundId = other.razorpayRefundId == null ? null : other.razorpayRefundId.copy();
        this.razorpayPaymentId = other.razorpayPaymentId == null ? null : other.razorpayPaymentId.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RefundsCriteria copy() {
        return new RefundsCriteria(this);
    }

    public IntegerFilter getId() {
        return id;
    }

    public IntegerFilter id() {
        if (id == null) {
            id = new IntegerFilter();
        }
        return id;
    }

    public void setId(IntegerFilter id) {
        this.id = id;
    }

    public IntegerFilter getOrderId() {
        return orderId;
    }

    public IntegerFilter orderId() {
        if (orderId == null) {
            orderId = new IntegerFilter();
        }
        return orderId;
    }

    public void setOrderId(IntegerFilter orderId) {
        this.orderId = orderId;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public StringFilter transactionId() {
        if (transactionId == null) {
            transactionId = new StringFilter();
        }
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public StringFilter getRazorpayRefundId() {
        return razorpayRefundId;
    }

    public StringFilter razorpayRefundId() {
        if (razorpayRefundId == null) {
            razorpayRefundId = new StringFilter();
        }
        return razorpayRefundId;
    }

    public void setRazorpayRefundId(StringFilter razorpayRefundId) {
        this.razorpayRefundId = razorpayRefundId;
    }

    public StringFilter getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public StringFilter razorpayPaymentId() {
        if (razorpayPaymentId == null) {
            razorpayPaymentId = new StringFilter();
        }
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(StringFilter razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RefundsCriteria that = (RefundsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(razorpayRefundId, that.razorpayRefundId) &&
            Objects.equals(razorpayPaymentId, that.razorpayPaymentId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            orderId,
            transactionId,
            razorpayRefundId,
            razorpayPaymentId,
            amount,
            currency,
            status,
            createdBy,
            createdAt,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (razorpayRefundId != null ? "razorpayRefundId=" + razorpayRefundId + ", " : "") +
            (razorpayPaymentId != null ? "razorpayPaymentId=" + razorpayPaymentId + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.SubscriptionRenewal} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.SubscriptionRenewalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscription-renewals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionRenewalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter userId;

    private LongFilter orderId;

    private LongFilter transactionId;

    private LocalDateFilter renewalDate;

    private FloatFilter paidRenewalAmount;

    private BooleanFilter status;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public SubscriptionRenewalCriteria() {}

    public SubscriptionRenewalCriteria(SubscriptionRenewalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.renewalDate = other.renewalDate == null ? null : other.renewalDate.copy();
        this.paidRenewalAmount = other.paidRenewalAmount == null ? null : other.paidRenewalAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubscriptionRenewalCriteria copy() {
        return new SubscriptionRenewalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getUserId() {
        return userId;
    }

    public IntegerFilter userId() {
        if (userId == null) {
            userId = new IntegerFilter();
        }
        return userId;
    }

    public void setUserId(IntegerFilter userId) {
        this.userId = userId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            transactionId = new LongFilter();
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateFilter getRenewalDate() {
        return renewalDate;
    }

    public LocalDateFilter renewalDate() {
        if (renewalDate == null) {
            renewalDate = new LocalDateFilter();
        }
        return renewalDate;
    }

    public void setRenewalDate(LocalDateFilter renewalDate) {
        this.renewalDate = renewalDate;
    }

    public FloatFilter getPaidRenewalAmount() {
        return paidRenewalAmount;
    }

    public FloatFilter paidRenewalAmount() {
        if (paidRenewalAmount == null) {
            paidRenewalAmount = new FloatFilter();
        }
        return paidRenewalAmount;
    }

    public void setPaidRenewalAmount(FloatFilter paidRenewalAmount) {
        this.paidRenewalAmount = paidRenewalAmount;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public BooleanFilter status() {
        if (status == null) {
            status = new BooleanFilter();
        }
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
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
        final SubscriptionRenewalCriteria that = (SubscriptionRenewalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(renewalDate, that.renewalDate) &&
            Objects.equals(paidRenewalAmount, that.paidRenewalAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            orderId,
            transactionId,
            renewalDate,
            paidRenewalAmount,
            status,
            startDate,
            endDate,
            createdAt,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionRenewalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (renewalDate != null ? "renewalDate=" + renewalDate + ", " : "") +
            (paidRenewalAmount != null ? "paidRenewalAmount=" + paidRenewalAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

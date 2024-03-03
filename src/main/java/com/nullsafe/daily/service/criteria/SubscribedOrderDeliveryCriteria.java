package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.SubscribedOrderDelivery} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.SubscribedOrderDeliveryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscribed-order-deliveries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrderDeliveryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private IntegerFilter paymentMode;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter orderId;

    private LongFilter entryUserId;

    private Boolean distinct;

    public SubscribedOrderDeliveryCriteria() {}

    public SubscribedOrderDeliveryCriteria(SubscribedOrderDeliveryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.paymentMode = other.paymentMode == null ? null : other.paymentMode.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.entryUserId = other.entryUserId == null ? null : other.entryUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubscribedOrderDeliveryCriteria copy() {
        return new SubscribedOrderDeliveryCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public IntegerFilter getPaymentMode() {
        return paymentMode;
    }

    public IntegerFilter paymentMode() {
        if (paymentMode == null) {
            paymentMode = new IntegerFilter();
        }
        return paymentMode;
    }

    public void setPaymentMode(IntegerFilter paymentMode) {
        this.paymentMode = paymentMode;
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

    public LongFilter getEntryUserId() {
        return entryUserId;
    }

    public LongFilter entryUserId() {
        if (entryUserId == null) {
            entryUserId = new LongFilter();
        }
        return entryUserId;
    }

    public void setEntryUserId(LongFilter entryUserId) {
        this.entryUserId = entryUserId;
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
        final SubscribedOrderDeliveryCriteria that = (SubscribedOrderDeliveryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(paymentMode, that.paymentMode) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(entryUserId, that.entryUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, paymentMode, createdAt, updatedAt, orderId, entryUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrderDeliveryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (paymentMode != null ? "paymentMode=" + paymentMode + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (entryUserId != null ? "entryUserId=" + entryUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

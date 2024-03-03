package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Transactions} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.TransactionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentId;

    private DoubleFilter amount;

    private StringFilter description;

    private IntegerFilter type;

    private IntegerFilter paymentMode;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter orderId;

    private LongFilter userId;

    private LongFilter ordersId;

    private LongFilter subscribedOrdersId;

    private Boolean distinct;

    public TransactionsCriteria() {}

    public TransactionsCriteria(TransactionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.paymentMode = other.paymentMode == null ? null : other.paymentMode.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.subscribedOrdersId = other.subscribedOrdersId == null ? null : other.subscribedOrdersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionsCriteria copy() {
        return new TransactionsCriteria(this);
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

    public StringFilter getPaymentId() {
        return paymentId;
    }

    public StringFilter paymentId() {
        if (paymentId == null) {
            paymentId = new StringFilter();
        }
        return paymentId;
    }

    public void setPaymentId(StringFilter paymentId) {
        this.paymentId = paymentId;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public DoubleFilter amount() {
        if (amount == null) {
            amount = new DoubleFilter();
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getType() {
        return type;
    }

    public IntegerFilter type() {
        if (type == null) {
            type = new IntegerFilter();
        }
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            ordersId = new LongFilter();
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getSubscribedOrdersId() {
        return subscribedOrdersId;
    }

    public LongFilter subscribedOrdersId() {
        if (subscribedOrdersId == null) {
            subscribedOrdersId = new LongFilter();
        }
        return subscribedOrdersId;
    }

    public void setSubscribedOrdersId(LongFilter subscribedOrdersId) {
        this.subscribedOrdersId = subscribedOrdersId;
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
        final TransactionsCriteria that = (TransactionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(paymentMode, that.paymentMode) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(subscribedOrdersId, that.subscribedOrdersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentId,
            amount,
            description,
            type,
            paymentMode,
            createdAt,
            updatedAt,
            orderId,
            userId,
            ordersId,
            subscribedOrdersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (paymentMode != null ? "paymentMode=" + paymentMode + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (subscribedOrdersId != null ? "subscribedOrdersId=" + subscribedOrdersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

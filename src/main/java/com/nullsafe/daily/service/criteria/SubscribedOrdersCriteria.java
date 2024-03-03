package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.SubscribedOrders} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.SubscribedOrdersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscribed-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrdersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter paymentType;

    private FloatFilter orderAmount;

    private FloatFilter subscriptionBalanceAmount;

    private FloatFilter price;

    private FloatFilter mrp;

    private FloatFilter tax;

    private IntegerFilter qty;

    private IntegerFilter offerId;

    private StringFilter selectedDaysForWeekly;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter lastRenewalDate;

    private IntegerFilter subscriptionType;

    private IntegerFilter approvalStatus;

    private BooleanFilter orderStatus;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private StringFilter createdBy;

    private StringFilter updatedBy;

    private LongFilter userId;

    private LongFilter transactionId;

    private LongFilter productId;

    private LongFilter addressId;

    private Boolean distinct;

    public SubscribedOrdersCriteria() {}

    public SubscribedOrdersCriteria(SubscribedOrdersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentType = other.paymentType == null ? null : other.paymentType.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.subscriptionBalanceAmount = other.subscriptionBalanceAmount == null ? null : other.subscriptionBalanceAmount.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.mrp = other.mrp == null ? null : other.mrp.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.offerId = other.offerId == null ? null : other.offerId.copy();
        this.selectedDaysForWeekly = other.selectedDaysForWeekly == null ? null : other.selectedDaysForWeekly.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.lastRenewalDate = other.lastRenewalDate == null ? null : other.lastRenewalDate.copy();
        this.subscriptionType = other.subscriptionType == null ? null : other.subscriptionType.copy();
        this.approvalStatus = other.approvalStatus == null ? null : other.approvalStatus.copy();
        this.orderStatus = other.orderStatus == null ? null : other.orderStatus.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubscribedOrdersCriteria copy() {
        return new SubscribedOrdersCriteria(this);
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

    public IntegerFilter getPaymentType() {
        return paymentType;
    }

    public IntegerFilter paymentType() {
        if (paymentType == null) {
            paymentType = new IntegerFilter();
        }
        return paymentType;
    }

    public void setPaymentType(IntegerFilter paymentType) {
        this.paymentType = paymentType;
    }

    public FloatFilter getOrderAmount() {
        return orderAmount;
    }

    public FloatFilter orderAmount() {
        if (orderAmount == null) {
            orderAmount = new FloatFilter();
        }
        return orderAmount;
    }

    public void setOrderAmount(FloatFilter orderAmount) {
        this.orderAmount = orderAmount;
    }

    public FloatFilter getSubscriptionBalanceAmount() {
        return subscriptionBalanceAmount;
    }

    public FloatFilter subscriptionBalanceAmount() {
        if (subscriptionBalanceAmount == null) {
            subscriptionBalanceAmount = new FloatFilter();
        }
        return subscriptionBalanceAmount;
    }

    public void setSubscriptionBalanceAmount(FloatFilter subscriptionBalanceAmount) {
        this.subscriptionBalanceAmount = subscriptionBalanceAmount;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public FloatFilter getMrp() {
        return mrp;
    }

    public FloatFilter mrp() {
        if (mrp == null) {
            mrp = new FloatFilter();
        }
        return mrp;
    }

    public void setMrp(FloatFilter mrp) {
        this.mrp = mrp;
    }

    public FloatFilter getTax() {
        return tax;
    }

    public FloatFilter tax() {
        if (tax == null) {
            tax = new FloatFilter();
        }
        return tax;
    }

    public void setTax(FloatFilter tax) {
        this.tax = tax;
    }

    public IntegerFilter getQty() {
        return qty;
    }

    public IntegerFilter qty() {
        if (qty == null) {
            qty = new IntegerFilter();
        }
        return qty;
    }

    public void setQty(IntegerFilter qty) {
        this.qty = qty;
    }

    public IntegerFilter getOfferId() {
        return offerId;
    }

    public IntegerFilter offerId() {
        if (offerId == null) {
            offerId = new IntegerFilter();
        }
        return offerId;
    }

    public void setOfferId(IntegerFilter offerId) {
        this.offerId = offerId;
    }

    public StringFilter getSelectedDaysForWeekly() {
        return selectedDaysForWeekly;
    }

    public StringFilter selectedDaysForWeekly() {
        if (selectedDaysForWeekly == null) {
            selectedDaysForWeekly = new StringFilter();
        }
        return selectedDaysForWeekly;
    }

    public void setSelectedDaysForWeekly(StringFilter selectedDaysForWeekly) {
        this.selectedDaysForWeekly = selectedDaysForWeekly;
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

    public LocalDateFilter getLastRenewalDate() {
        return lastRenewalDate;
    }

    public LocalDateFilter lastRenewalDate() {
        if (lastRenewalDate == null) {
            lastRenewalDate = new LocalDateFilter();
        }
        return lastRenewalDate;
    }

    public void setLastRenewalDate(LocalDateFilter lastRenewalDate) {
        this.lastRenewalDate = lastRenewalDate;
    }

    public IntegerFilter getSubscriptionType() {
        return subscriptionType;
    }

    public IntegerFilter subscriptionType() {
        if (subscriptionType == null) {
            subscriptionType = new IntegerFilter();
        }
        return subscriptionType;
    }

    public void setSubscriptionType(IntegerFilter subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public IntegerFilter getApprovalStatus() {
        return approvalStatus;
    }

    public IntegerFilter approvalStatus() {
        if (approvalStatus == null) {
            approvalStatus = new IntegerFilter();
        }
        return approvalStatus;
    }

    public void setApprovalStatus(IntegerFilter approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public BooleanFilter getOrderStatus() {
        return orderStatus;
    }

    public BooleanFilter orderStatus() {
        if (orderStatus == null) {
            orderStatus = new BooleanFilter();
        }
        return orderStatus;
    }

    public void setOrderStatus(BooleanFilter orderStatus) {
        this.orderStatus = orderStatus;
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

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            updatedBy = new StringFilter();
        }
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
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

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
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
        final SubscribedOrdersCriteria that = (SubscribedOrdersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentType, that.paymentType) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(subscriptionBalanceAmount, that.subscriptionBalanceAmount) &&
            Objects.equals(price, that.price) &&
            Objects.equals(mrp, that.mrp) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(offerId, that.offerId) &&
            Objects.equals(selectedDaysForWeekly, that.selectedDaysForWeekly) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(lastRenewalDate, that.lastRenewalDate) &&
            Objects.equals(subscriptionType, that.subscriptionType) &&
            Objects.equals(approvalStatus, that.approvalStatus) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentType,
            orderAmount,
            subscriptionBalanceAmount,
            price,
            mrp,
            tax,
            qty,
            offerId,
            selectedDaysForWeekly,
            startDate,
            endDate,
            lastRenewalDate,
            subscriptionType,
            approvalStatus,
            orderStatus,
            createdAt,
            updatedAt,
            createdBy,
            updatedBy,
            userId,
            transactionId,
            productId,
            addressId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrdersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentType != null ? "paymentType=" + paymentType + ", " : "") +
            (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
            (subscriptionBalanceAmount != null ? "subscriptionBalanceAmount=" + subscriptionBalanceAmount + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (mrp != null ? "mrp=" + mrp + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (offerId != null ? "offerId=" + offerId + ", " : "") +
            (selectedDaysForWeekly != null ? "selectedDaysForWeekly=" + selectedDaysForWeekly + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (lastRenewalDate != null ? "lastRenewalDate=" + lastRenewalDate + ", " : "") +
            (subscriptionType != null ? "subscriptionType=" + subscriptionType + ", " : "") +
            (approvalStatus != null ? "approvalStatus=" + approvalStatus + ", " : "") +
            (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

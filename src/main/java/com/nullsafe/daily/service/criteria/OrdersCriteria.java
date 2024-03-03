package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Orders} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.OrdersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter orderType;

    private DoubleFilter orderAmount;

    private DoubleFilter price;

    private DoubleFilter mrp;

    private DoubleFilter tax;

    private IntegerFilter qty;

    private StringFilter selectedDaysForWeekly;

    private LocalDateFilter startDate;

    private IntegerFilter subscriptionType;

    private IntegerFilter status;

    private IntegerFilter deliveryStatus;

    private BooleanFilter orderStatus;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter userId;

    private LongFilter trasationId;

    private LongFilter productId;

    private LongFilter addressId;

    private LongFilter orderUserAssignId;

    private LongFilter subscribedOrderDeliveryId;

    private LongFilter transactionsId;

    private Boolean distinct;

    public OrdersCriteria() {}

    public OrdersCriteria(OrdersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderType = other.orderType == null ? null : other.orderType.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.mrp = other.mrp == null ? null : other.mrp.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.selectedDaysForWeekly = other.selectedDaysForWeekly == null ? null : other.selectedDaysForWeekly.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.subscriptionType = other.subscriptionType == null ? null : other.subscriptionType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.deliveryStatus = other.deliveryStatus == null ? null : other.deliveryStatus.copy();
        this.orderStatus = other.orderStatus == null ? null : other.orderStatus.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.trasationId = other.trasationId == null ? null : other.trasationId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.orderUserAssignId = other.orderUserAssignId == null ? null : other.orderUserAssignId.copy();
        this.subscribedOrderDeliveryId = other.subscribedOrderDeliveryId == null ? null : other.subscribedOrderDeliveryId.copy();
        this.transactionsId = other.transactionsId == null ? null : other.transactionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrdersCriteria copy() {
        return new OrdersCriteria(this);
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

    public IntegerFilter getOrderType() {
        return orderType;
    }

    public IntegerFilter orderType() {
        if (orderType == null) {
            orderType = new IntegerFilter();
        }
        return orderType;
    }

    public void setOrderType(IntegerFilter orderType) {
        this.orderType = orderType;
    }

    public DoubleFilter getOrderAmount() {
        return orderAmount;
    }

    public DoubleFilter orderAmount() {
        if (orderAmount == null) {
            orderAmount = new DoubleFilter();
        }
        return orderAmount;
    }

    public void setOrderAmount(DoubleFilter orderAmount) {
        this.orderAmount = orderAmount;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public DoubleFilter getMrp() {
        return mrp;
    }

    public DoubleFilter mrp() {
        if (mrp == null) {
            mrp = new DoubleFilter();
        }
        return mrp;
    }

    public void setMrp(DoubleFilter mrp) {
        this.mrp = mrp;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
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

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public IntegerFilter getDeliveryStatus() {
        return deliveryStatus;
    }

    public IntegerFilter deliveryStatus() {
        if (deliveryStatus == null) {
            deliveryStatus = new IntegerFilter();
        }
        return deliveryStatus;
    }

    public void setDeliveryStatus(IntegerFilter deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public LongFilter getTrasationId() {
        return trasationId;
    }

    public LongFilter trasationId() {
        if (trasationId == null) {
            trasationId = new LongFilter();
        }
        return trasationId;
    }

    public void setTrasationId(LongFilter trasationId) {
        this.trasationId = trasationId;
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

    public LongFilter getOrderUserAssignId() {
        return orderUserAssignId;
    }

    public LongFilter orderUserAssignId() {
        if (orderUserAssignId == null) {
            orderUserAssignId = new LongFilter();
        }
        return orderUserAssignId;
    }

    public void setOrderUserAssignId(LongFilter orderUserAssignId) {
        this.orderUserAssignId = orderUserAssignId;
    }

    public LongFilter getSubscribedOrderDeliveryId() {
        return subscribedOrderDeliveryId;
    }

    public LongFilter subscribedOrderDeliveryId() {
        if (subscribedOrderDeliveryId == null) {
            subscribedOrderDeliveryId = new LongFilter();
        }
        return subscribedOrderDeliveryId;
    }

    public void setSubscribedOrderDeliveryId(LongFilter subscribedOrderDeliveryId) {
        this.subscribedOrderDeliveryId = subscribedOrderDeliveryId;
    }

    public LongFilter getTransactionsId() {
        return transactionsId;
    }

    public LongFilter transactionsId() {
        if (transactionsId == null) {
            transactionsId = new LongFilter();
        }
        return transactionsId;
    }

    public void setTransactionsId(LongFilter transactionsId) {
        this.transactionsId = transactionsId;
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
        final OrdersCriteria that = (OrdersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderType, that.orderType) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(price, that.price) &&
            Objects.equals(mrp, that.mrp) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(selectedDaysForWeekly, that.selectedDaysForWeekly) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(subscriptionType, that.subscriptionType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(deliveryStatus, that.deliveryStatus) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(trasationId, that.trasationId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(orderUserAssignId, that.orderUserAssignId) &&
            Objects.equals(subscribedOrderDeliveryId, that.subscribedOrderDeliveryId) &&
            Objects.equals(transactionsId, that.transactionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            orderType,
            orderAmount,
            price,
            mrp,
            tax,
            qty,
            selectedDaysForWeekly,
            startDate,
            subscriptionType,
            status,
            deliveryStatus,
            orderStatus,
            createdAt,
            updatedAt,
            userId,
            trasationId,
            productId,
            addressId,
            orderUserAssignId,
            subscribedOrderDeliveryId,
            transactionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderType != null ? "orderType=" + orderType + ", " : "") +
            (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (mrp != null ? "mrp=" + mrp + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (selectedDaysForWeekly != null ? "selectedDaysForWeekly=" + selectedDaysForWeekly + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (subscriptionType != null ? "subscriptionType=" + subscriptionType + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (deliveryStatus != null ? "deliveryStatus=" + deliveryStatus + ", " : "") +
            (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (trasationId != null ? "trasationId=" + trasationId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (orderUserAssignId != null ? "orderUserAssignId=" + orderUserAssignId + ", " : "") +
            (subscribedOrderDeliveryId != null ? "subscribedOrderDeliveryId=" + subscribedOrderDeliveryId + ", " : "") +
            (transactionsId != null ? "transactionsId=" + transactionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Users} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.UsersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter walletAmount;

    private StringFilter email;

    private StringFilter phone;

    private InstantFilter emailVerifiedAt;

    private StringFilter password;

    private StringFilter rememberToken;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private StringFilter name;

    private StringFilter fcm;

    private IntegerFilter subscriptionAmount;

    private LongFilter assignRoleId;

    private LongFilter cartId;

    private LongFilter orderUserAssignId;

    private LongFilter ordersId;

    private LongFilter specificNotificationId;

    private LongFilter subscribedOrderDeliveryId;

    private LongFilter subscribedOrdersId;

    private LongFilter transactionsId;

    private Boolean distinct;

    public UsersCriteria() {}

    public UsersCriteria(UsersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.walletAmount = other.walletAmount == null ? null : other.walletAmount.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.emailVerifiedAt = other.emailVerifiedAt == null ? null : other.emailVerifiedAt.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.rememberToken = other.rememberToken == null ? null : other.rememberToken.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fcm = other.fcm == null ? null : other.fcm.copy();
        this.subscriptionAmount = other.subscriptionAmount == null ? null : other.subscriptionAmount.copy();
        this.assignRoleId = other.assignRoleId == null ? null : other.assignRoleId.copy();
        this.cartId = other.cartId == null ? null : other.cartId.copy();
        this.orderUserAssignId = other.orderUserAssignId == null ? null : other.orderUserAssignId.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.specificNotificationId = other.specificNotificationId == null ? null : other.specificNotificationId.copy();
        this.subscribedOrderDeliveryId = other.subscribedOrderDeliveryId == null ? null : other.subscribedOrderDeliveryId.copy();
        this.subscribedOrdersId = other.subscribedOrdersId == null ? null : other.subscribedOrdersId.copy();
        this.transactionsId = other.transactionsId == null ? null : other.transactionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsersCriteria copy() {
        return new UsersCriteria(this);
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

    public DoubleFilter getWalletAmount() {
        return walletAmount;
    }

    public DoubleFilter walletAmount() {
        if (walletAmount == null) {
            walletAmount = new DoubleFilter();
        }
        return walletAmount;
    }

    public void setWalletAmount(DoubleFilter walletAmount) {
        this.walletAmount = walletAmount;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public InstantFilter getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public InstantFilter emailVerifiedAt() {
        if (emailVerifiedAt == null) {
            emailVerifiedAt = new InstantFilter();
        }
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(InstantFilter emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getRememberToken() {
        return rememberToken;
    }

    public StringFilter rememberToken() {
        if (rememberToken == null) {
            rememberToken = new StringFilter();
        }
        return rememberToken;
    }

    public void setRememberToken(StringFilter rememberToken) {
        this.rememberToken = rememberToken;
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFcm() {
        return fcm;
    }

    public StringFilter fcm() {
        if (fcm == null) {
            fcm = new StringFilter();
        }
        return fcm;
    }

    public void setFcm(StringFilter fcm) {
        this.fcm = fcm;
    }

    public IntegerFilter getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public IntegerFilter subscriptionAmount() {
        if (subscriptionAmount == null) {
            subscriptionAmount = new IntegerFilter();
        }
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(IntegerFilter subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public LongFilter getAssignRoleId() {
        return assignRoleId;
    }

    public LongFilter assignRoleId() {
        if (assignRoleId == null) {
            assignRoleId = new LongFilter();
        }
        return assignRoleId;
    }

    public void setAssignRoleId(LongFilter assignRoleId) {
        this.assignRoleId = assignRoleId;
    }

    public LongFilter getCartId() {
        return cartId;
    }

    public LongFilter cartId() {
        if (cartId == null) {
            cartId = new LongFilter();
        }
        return cartId;
    }

    public void setCartId(LongFilter cartId) {
        this.cartId = cartId;
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

    public LongFilter getSpecificNotificationId() {
        return specificNotificationId;
    }

    public LongFilter specificNotificationId() {
        if (specificNotificationId == null) {
            specificNotificationId = new LongFilter();
        }
        return specificNotificationId;
    }

    public void setSpecificNotificationId(LongFilter specificNotificationId) {
        this.specificNotificationId = specificNotificationId;
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
        final UsersCriteria that = (UsersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(walletAmount, that.walletAmount) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(emailVerifiedAt, that.emailVerifiedAt) &&
            Objects.equals(password, that.password) &&
            Objects.equals(rememberToken, that.rememberToken) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fcm, that.fcm) &&
            Objects.equals(subscriptionAmount, that.subscriptionAmount) &&
            Objects.equals(assignRoleId, that.assignRoleId) &&
            Objects.equals(cartId, that.cartId) &&
            Objects.equals(orderUserAssignId, that.orderUserAssignId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(specificNotificationId, that.specificNotificationId) &&
            Objects.equals(subscribedOrderDeliveryId, that.subscribedOrderDeliveryId) &&
            Objects.equals(subscribedOrdersId, that.subscribedOrdersId) &&
            Objects.equals(transactionsId, that.transactionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            walletAmount,
            email,
            phone,
            emailVerifiedAt,
            password,
            rememberToken,
            createdAt,
            updatedAt,
            name,
            fcm,
            subscriptionAmount,
            assignRoleId,
            cartId,
            orderUserAssignId,
            ordersId,
            specificNotificationId,
            subscribedOrderDeliveryId,
            subscribedOrdersId,
            transactionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (walletAmount != null ? "walletAmount=" + walletAmount + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (emailVerifiedAt != null ? "emailVerifiedAt=" + emailVerifiedAt + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (rememberToken != null ? "rememberToken=" + rememberToken + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (fcm != null ? "fcm=" + fcm + ", " : "") +
            (subscriptionAmount != null ? "subscriptionAmount=" + subscriptionAmount + ", " : "") +
            (assignRoleId != null ? "assignRoleId=" + assignRoleId + ", " : "") +
            (cartId != null ? "cartId=" + cartId + ", " : "") +
            (orderUserAssignId != null ? "orderUserAssignId=" + orderUserAssignId + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (specificNotificationId != null ? "specificNotificationId=" + specificNotificationId + ", " : "") +
            (subscribedOrderDeliveryId != null ? "subscribedOrderDeliveryId=" + subscribedOrderDeliveryId + ", " : "") +
            (subscribedOrdersId != null ? "subscribedOrdersId=" + subscribedOrdersId + ", " : "") +
            (transactionsId != null ? "transactionsId=" + transactionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

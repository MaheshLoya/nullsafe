package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A SubscribedOrders.
 */
@Entity
@Table(name = "subscribed_orders")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * 1&#61; prepaid-online,2&#61;prepaid-cash, 3&#61;postpaid-online,4&#61;postpaid-cash
     */
    @Column(name = "payment_type")
    private Integer paymentType;

    @NotNull
    @Column(name = "order_amount", nullable = false)
    private Float orderAmount;

    @Column(name = "subscription_balance_amount")
    private Float subscriptionBalanceAmount;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @NotNull
    @Column(name = "mrp", nullable = false)
    private Float mrp;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Float tax;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "offer_id")
    private Integer offerId;

    @Size(max = 65535)
    @Column(name = "selected_days_for_weekly", length = 65535)
    private String selectedDaysForWeekly;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "last_renewal_date")
    private LocalDate lastRenewalDate;

    /**
     * 1&#61;daily,2&#61;weekly,3&#61;monthly,4&#61;alternative days
     */
    @Column(name = "subscription_type")
    private Integer subscriptionType;

    /**
     * 0&#61;pending, 1&#61;confirmed, 2&#61;canceled
     */
    @NotNull
    @Column(name = "approval_status", nullable = false)
    private Integer approvalStatus;

    /**
     * 0&#61;active,1&#61;stop
     */
    @NotNull
    @Column(name = "order_status", nullable = false)
    private Boolean orderStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Size(max = 255)
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "assignRoles",
            "carts",
            "orderUserAssigns",
            "orders",
            "specificNotifications",
            "subscribedOrderDeliveries",
            "subscribedOrders",
            "transactions",
        },
        allowSetters = true
    )
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "order", "user", "orders", "subscribedOrders" }, allowSetters = true)
    private Transactions transaction;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subCat", "carts", "orders", "subscribedOrders" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "orders", "subscribedOrders" }, allowSetters = true)
    private UserAddress address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscribedOrders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaymentType() {
        return this.paymentType;
    }

    public SubscribedOrders paymentType(Integer paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Float getOrderAmount() {
        return this.orderAmount;
    }

    public SubscribedOrders orderAmount(Float orderAmount) {
        this.setOrderAmount(orderAmount);
        return this;
    }

    public void setOrderAmount(Float orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Float getSubscriptionBalanceAmount() {
        return this.subscriptionBalanceAmount;
    }

    public SubscribedOrders subscriptionBalanceAmount(Float subscriptionBalanceAmount) {
        this.setSubscriptionBalanceAmount(subscriptionBalanceAmount);
        return this;
    }

    public void setSubscriptionBalanceAmount(Float subscriptionBalanceAmount) {
        this.subscriptionBalanceAmount = subscriptionBalanceAmount;
    }

    public Float getPrice() {
        return this.price;
    }

    public SubscribedOrders price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getMrp() {
        return this.mrp;
    }

    public SubscribedOrders mrp(Float mrp) {
        this.setMrp(mrp);
        return this;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public Float getTax() {
        return this.tax;
    }

    public SubscribedOrders tax(Float tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Integer getQty() {
        return this.qty;
    }

    public SubscribedOrders qty(Integer qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getOfferId() {
        return this.offerId;
    }

    public SubscribedOrders offerId(Integer offerId) {
        this.setOfferId(offerId);
        return this;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getSelectedDaysForWeekly() {
        return this.selectedDaysForWeekly;
    }

    public SubscribedOrders selectedDaysForWeekly(String selectedDaysForWeekly) {
        this.setSelectedDaysForWeekly(selectedDaysForWeekly);
        return this;
    }

    public void setSelectedDaysForWeekly(String selectedDaysForWeekly) {
        this.selectedDaysForWeekly = selectedDaysForWeekly;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public SubscribedOrders startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public SubscribedOrders endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getLastRenewalDate() {
        return this.lastRenewalDate;
    }

    public SubscribedOrders lastRenewalDate(LocalDate lastRenewalDate) {
        this.setLastRenewalDate(lastRenewalDate);
        return this;
    }

    public void setLastRenewalDate(LocalDate lastRenewalDate) {
        this.lastRenewalDate = lastRenewalDate;
    }

    public Integer getSubscriptionType() {
        return this.subscriptionType;
    }

    public SubscribedOrders subscriptionType(Integer subscriptionType) {
        this.setSubscriptionType(subscriptionType);
        return this;
    }

    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getApprovalStatus() {
        return this.approvalStatus;
    }

    public SubscribedOrders approvalStatus(Integer approvalStatus) {
        this.setApprovalStatus(approvalStatus);
        return this;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Boolean getOrderStatus() {
        return this.orderStatus;
    }

    public SubscribedOrders orderStatus(Boolean orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SubscribedOrders createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SubscribedOrders updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SubscribedOrders createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public SubscribedOrders updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    public SubscribedOrders user(Users users) {
        this.setUser(users);
        return this;
    }

    public Transactions getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transactions transactions) {
        this.transaction = transactions;
    }

    public SubscribedOrders transaction(Transactions transactions) {
        this.setTransaction(transactions);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SubscribedOrders product(Product product) {
        this.setProduct(product);
        return this;
    }

    public UserAddress getAddress() {
        return this.address;
    }

    public void setAddress(UserAddress userAddress) {
        this.address = userAddress;
    }

    public SubscribedOrders address(UserAddress userAddress) {
        this.setAddress(userAddress);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribedOrders)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscribedOrders) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrders{" +
            "id=" + getId() +
            ", paymentType=" + getPaymentType() +
            ", orderAmount=" + getOrderAmount() +
            ", subscriptionBalanceAmount=" + getSubscriptionBalanceAmount() +
            ", price=" + getPrice() +
            ", mrp=" + getMrp() +
            ", tax=" + getTax() +
            ", qty=" + getQty() +
            ", offerId=" + getOfferId() +
            ", selectedDaysForWeekly='" + getSelectedDaysForWeekly() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", lastRenewalDate='" + getLastRenewalDate() + "'" +
            ", subscriptionType=" + getSubscriptionType() +
            ", approvalStatus=" + getApprovalStatus() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}

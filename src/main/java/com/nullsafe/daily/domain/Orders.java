package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * 1&#61; prepaid,2&#61;pos, 3&#61;pay now,4&#61;cod&#13;&#10;
     */
    @Column(name = "order_type")
    private Integer orderType;

    @NotNull
    @Column(name = "order_amount", nullable = false)
    private Double orderAmount;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "mrp", nullable = false)
    private Double mrp;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Double tax;

    @Column(name = "qty")
    private Integer qty;

    @Size(max = 65535)
    @Column(name = "selected_days_for_weekly", length = 65535)
    private String selectedDaysForWeekly;

    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * 1&#61;daliy,2&#61;weekly,3&#61;monthly,4&#61;alternative days&#13;&#10;
     */
    @Column(name = "subscription_type")
    private Integer subscriptionType;

    /**
     * 1&#61;confirmed 1&#61;confirmed, 0&#61;pending,2&#61;canceled
     */
    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 1&#61;delivered
     */
    @Column(name = "delivery_status")
    private Integer deliveryStatus;

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
    private Transactions trasation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subCat", "carts", "orders", "subscribedOrders" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "orders", "subscribedOrders" }, allowSetters = true)
    private UserAddress address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order", "user" }, allowSetters = true)
    private Set<OrderUserAssign> orderUserAssigns = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order", "entryUser" }, allowSetters = true)
    private Set<SubscribedOrderDelivery> subscribedOrderDeliveries = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order", "user", "orders", "subscribedOrders" }, allowSetters = true)
    private Set<Transactions> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderType() {
        return this.orderType;
    }

    public Orders orderType(Integer orderType) {
        this.setOrderType(orderType);
        return this;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Double getOrderAmount() {
        return this.orderAmount;
    }

    public Orders orderAmount(Double orderAmount) {
        this.setOrderAmount(orderAmount);
        return this;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPrice() {
        return this.price;
    }

    public Orders price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMrp() {
        return this.mrp;
    }

    public Orders mrp(Double mrp) {
        this.setMrp(mrp);
        return this;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getTax() {
        return this.tax;
    }

    public Orders tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Integer getQty() {
        return this.qty;
    }

    public Orders qty(Integer qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getSelectedDaysForWeekly() {
        return this.selectedDaysForWeekly;
    }

    public Orders selectedDaysForWeekly(String selectedDaysForWeekly) {
        this.setSelectedDaysForWeekly(selectedDaysForWeekly);
        return this;
    }

    public void setSelectedDaysForWeekly(String selectedDaysForWeekly) {
        this.selectedDaysForWeekly = selectedDaysForWeekly;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Orders startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getSubscriptionType() {
        return this.subscriptionType;
    }

    public Orders subscriptionType(Integer subscriptionType) {
        this.setSubscriptionType(subscriptionType);
        return this;
    }

    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Orders status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public Orders deliveryStatus(Integer deliveryStatus) {
        this.setDeliveryStatus(deliveryStatus);
        return this;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Boolean getOrderStatus() {
        return this.orderStatus;
    }

    public Orders orderStatus(Boolean orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Orders createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Orders updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    public Orders user(Users users) {
        this.setUser(users);
        return this;
    }

    public Transactions getTrasation() {
        return this.trasation;
    }

    public void setTrasation(Transactions transactions) {
        this.trasation = transactions;
    }

    public Orders trasation(Transactions transactions) {
        this.setTrasation(transactions);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders product(Product product) {
        this.setProduct(product);
        return this;
    }

    public UserAddress getAddress() {
        return this.address;
    }

    public void setAddress(UserAddress userAddress) {
        this.address = userAddress;
    }

    public Orders address(UserAddress userAddress) {
        this.setAddress(userAddress);
        return this;
    }

    public Set<OrderUserAssign> getOrderUserAssigns() {
        return this.orderUserAssigns;
    }

    public void setOrderUserAssigns(Set<OrderUserAssign> orderUserAssigns) {
        if (this.orderUserAssigns != null) {
            this.orderUserAssigns.forEach(i -> i.setOrder(null));
        }
        if (orderUserAssigns != null) {
            orderUserAssigns.forEach(i -> i.setOrder(this));
        }
        this.orderUserAssigns = orderUserAssigns;
    }

    public Orders orderUserAssigns(Set<OrderUserAssign> orderUserAssigns) {
        this.setOrderUserAssigns(orderUserAssigns);
        return this;
    }

    public Orders addOrderUserAssign(OrderUserAssign orderUserAssign) {
        this.orderUserAssigns.add(orderUserAssign);
        orderUserAssign.setOrder(this);
        return this;
    }

    public Orders removeOrderUserAssign(OrderUserAssign orderUserAssign) {
        this.orderUserAssigns.remove(orderUserAssign);
        orderUserAssign.setOrder(null);
        return this;
    }

    public Set<SubscribedOrderDelivery> getSubscribedOrderDeliveries() {
        return this.subscribedOrderDeliveries;
    }

    public void setSubscribedOrderDeliveries(Set<SubscribedOrderDelivery> subscribedOrderDeliveries) {
        if (this.subscribedOrderDeliveries != null) {
            this.subscribedOrderDeliveries.forEach(i -> i.setOrder(null));
        }
        if (subscribedOrderDeliveries != null) {
            subscribedOrderDeliveries.forEach(i -> i.setOrder(this));
        }
        this.subscribedOrderDeliveries = subscribedOrderDeliveries;
    }

    public Orders subscribedOrderDeliveries(Set<SubscribedOrderDelivery> subscribedOrderDeliveries) {
        this.setSubscribedOrderDeliveries(subscribedOrderDeliveries);
        return this;
    }

    public Orders addSubscribedOrderDelivery(SubscribedOrderDelivery subscribedOrderDelivery) {
        this.subscribedOrderDeliveries.add(subscribedOrderDelivery);
        subscribedOrderDelivery.setOrder(this);
        return this;
    }

    public Orders removeSubscribedOrderDelivery(SubscribedOrderDelivery subscribedOrderDelivery) {
        this.subscribedOrderDeliveries.remove(subscribedOrderDelivery);
        subscribedOrderDelivery.setOrder(null);
        return this;
    }

    public Set<Transactions> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transactions> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setOrder(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setOrder(this));
        }
        this.transactions = transactions;
    }

    public Orders transactions(Set<Transactions> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Orders addTransactions(Transactions transactions) {
        this.transactions.add(transactions);
        transactions.setOrder(this);
        return this;
    }

    public Orders removeTransactions(Transactions transactions) {
        this.transactions.remove(transactions);
        transactions.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orders)) {
            return false;
        }
        return getId() != null && getId().equals(((Orders) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", orderType=" + getOrderType() +
            ", orderAmount=" + getOrderAmount() +
            ", price=" + getPrice() +
            ", mrp=" + getMrp() +
            ", tax=" + getTax() +
            ", qty=" + getQty() +
            ", selectedDaysForWeekly='" + getSelectedDaysForWeekly() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", subscriptionType=" + getSubscriptionType() +
            ", status=" + getStatus() +
            ", deliveryStatus=" + getDeliveryStatus() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "wallet_amount")
    private Double walletAmount;

    @Size(max = 255)
    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Size(max = 250)
    @Column(name = "phone", length = 250, unique = true)
    private String phone;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt;

    @Size(max = 255)
    @Column(name = "password", length = 255)
    private String password;

    @Size(max = 100)
    @Column(name = "remember_token", length = 100)
    private String rememberToken;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Size(max = 250)
    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Size(max = 65535)
    @Column(name = "fcm", length = 65535)
    private String fcm;

    @NotNull
    @Column(name = "subscription_amount", nullable = false)
    private Integer subscriptionAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "user", "role" }, allowSetters = true)
    private Set<AssignRole> assignRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "product", "user" }, allowSetters = true)
    private Set<Cart> carts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "order", "user" }, allowSetters = true)
    private Set<OrderUserAssign> orderUserAssigns = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(
        value = { "user", "trasation", "product", "address", "orderUserAssigns", "subscribedOrderDeliveries", "transactions" },
        allowSetters = true
    )
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<SpecificNotification> specificNotifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entryUser")
    @JsonIgnoreProperties(value = { "order", "entryUser" }, allowSetters = true)
    private Set<SubscribedOrderDelivery> subscribedOrderDeliveries = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "user", "transaction", "product", "address" }, allowSetters = true)
    private Set<SubscribedOrders> subscribedOrders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "order", "user", "orders", "subscribedOrders" }, allowSetters = true)
    private Set<Transactions> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWalletAmount() {
        return this.walletAmount;
    }

    public Users walletAmount(Double walletAmount) {
        this.setWalletAmount(walletAmount);
        return this;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getEmail() {
        return this.email;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Users phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getEmailVerifiedAt() {
        return this.emailVerifiedAt;
    }

    public Users emailVerifiedAt(Instant emailVerifiedAt) {
        this.setEmailVerifiedAt(emailVerifiedAt);
        return this;
    }

    public void setEmailVerifiedAt(Instant emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPassword() {
        return this.password;
    }

    public Users password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberToken() {
        return this.rememberToken;
    }

    public Users rememberToken(String rememberToken) {
        this.setRememberToken(rememberToken);
        return this;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Users createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Users updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return this.name;
    }

    public Users name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFcm() {
        return this.fcm;
    }

    public Users fcm(String fcm) {
        this.setFcm(fcm);
        return this;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public Integer getSubscriptionAmount() {
        return this.subscriptionAmount;
    }

    public Users subscriptionAmount(Integer subscriptionAmount) {
        this.setSubscriptionAmount(subscriptionAmount);
        return this;
    }

    public void setSubscriptionAmount(Integer subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Set<AssignRole> getAssignRoles() {
        return this.assignRoles;
    }

    public void setAssignRoles(Set<AssignRole> assignRoles) {
        if (this.assignRoles != null) {
            this.assignRoles.forEach(i -> i.setUser(null));
        }
        if (assignRoles != null) {
            assignRoles.forEach(i -> i.setUser(this));
        }
        this.assignRoles = assignRoles;
    }

    public Users assignRoles(Set<AssignRole> assignRoles) {
        this.setAssignRoles(assignRoles);
        return this;
    }

    public Users addAssignRole(AssignRole assignRole) {
        this.assignRoles.add(assignRole);
        assignRole.setUser(this);
        return this;
    }

    public Users removeAssignRole(AssignRole assignRole) {
        this.assignRoles.remove(assignRole);
        assignRole.setUser(null);
        return this;
    }

    public Set<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(Set<Cart> carts) {
        if (this.carts != null) {
            this.carts.forEach(i -> i.setUser(null));
        }
        if (carts != null) {
            carts.forEach(i -> i.setUser(this));
        }
        this.carts = carts;
    }

    public Users carts(Set<Cart> carts) {
        this.setCarts(carts);
        return this;
    }

    public Users addCart(Cart cart) {
        this.carts.add(cart);
        cart.setUser(this);
        return this;
    }

    public Users removeCart(Cart cart) {
        this.carts.remove(cart);
        cart.setUser(null);
        return this;
    }

    public Set<OrderUserAssign> getOrderUserAssigns() {
        return this.orderUserAssigns;
    }

    public void setOrderUserAssigns(Set<OrderUserAssign> orderUserAssigns) {
        if (this.orderUserAssigns != null) {
            this.orderUserAssigns.forEach(i -> i.setUser(null));
        }
        if (orderUserAssigns != null) {
            orderUserAssigns.forEach(i -> i.setUser(this));
        }
        this.orderUserAssigns = orderUserAssigns;
    }

    public Users orderUserAssigns(Set<OrderUserAssign> orderUserAssigns) {
        this.setOrderUserAssigns(orderUserAssigns);
        return this;
    }

    public Users addOrderUserAssign(OrderUserAssign orderUserAssign) {
        this.orderUserAssigns.add(orderUserAssign);
        orderUserAssign.setUser(this);
        return this;
    }

    public Users removeOrderUserAssign(OrderUserAssign orderUserAssign) {
        this.orderUserAssigns.remove(orderUserAssign);
        orderUserAssign.setUser(null);
        return this;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setUser(this));
        }
        this.orders = orders;
    }

    public Users orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public Users addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setUser(this);
        return this;
    }

    public Users removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setUser(null);
        return this;
    }

    public Set<SpecificNotification> getSpecificNotifications() {
        return this.specificNotifications;
    }

    public void setSpecificNotifications(Set<SpecificNotification> specificNotifications) {
        if (this.specificNotifications != null) {
            this.specificNotifications.forEach(i -> i.setUser(null));
        }
        if (specificNotifications != null) {
            specificNotifications.forEach(i -> i.setUser(this));
        }
        this.specificNotifications = specificNotifications;
    }

    public Users specificNotifications(Set<SpecificNotification> specificNotifications) {
        this.setSpecificNotifications(specificNotifications);
        return this;
    }

    public Users addSpecificNotification(SpecificNotification specificNotification) {
        this.specificNotifications.add(specificNotification);
        specificNotification.setUser(this);
        return this;
    }

    public Users removeSpecificNotification(SpecificNotification specificNotification) {
        this.specificNotifications.remove(specificNotification);
        specificNotification.setUser(null);
        return this;
    }

    public Set<SubscribedOrderDelivery> getSubscribedOrderDeliveries() {
        return this.subscribedOrderDeliveries;
    }

    public void setSubscribedOrderDeliveries(Set<SubscribedOrderDelivery> subscribedOrderDeliveries) {
        if (this.subscribedOrderDeliveries != null) {
            this.subscribedOrderDeliveries.forEach(i -> i.setEntryUser(null));
        }
        if (subscribedOrderDeliveries != null) {
            subscribedOrderDeliveries.forEach(i -> i.setEntryUser(this));
        }
        this.subscribedOrderDeliveries = subscribedOrderDeliveries;
    }

    public Users subscribedOrderDeliveries(Set<SubscribedOrderDelivery> subscribedOrderDeliveries) {
        this.setSubscribedOrderDeliveries(subscribedOrderDeliveries);
        return this;
    }

    public Users addSubscribedOrderDelivery(SubscribedOrderDelivery subscribedOrderDelivery) {
        this.subscribedOrderDeliveries.add(subscribedOrderDelivery);
        subscribedOrderDelivery.setEntryUser(this);
        return this;
    }

    public Users removeSubscribedOrderDelivery(SubscribedOrderDelivery subscribedOrderDelivery) {
        this.subscribedOrderDeliveries.remove(subscribedOrderDelivery);
        subscribedOrderDelivery.setEntryUser(null);
        return this;
    }

    public Set<SubscribedOrders> getSubscribedOrders() {
        return this.subscribedOrders;
    }

    public void setSubscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        if (this.subscribedOrders != null) {
            this.subscribedOrders.forEach(i -> i.setUser(null));
        }
        if (subscribedOrders != null) {
            subscribedOrders.forEach(i -> i.setUser(this));
        }
        this.subscribedOrders = subscribedOrders;
    }

    public Users subscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        this.setSubscribedOrders(subscribedOrders);
        return this;
    }

    public Users addSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.add(subscribedOrders);
        subscribedOrders.setUser(this);
        return this;
    }

    public Users removeSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.remove(subscribedOrders);
        subscribedOrders.setUser(null);
        return this;
    }

    public Set<Transactions> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transactions> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setUser(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setUser(this));
        }
        this.transactions = transactions;
    }

    public Users transactions(Set<Transactions> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Users addTransactions(Transactions transactions) {
        this.transactions.add(transactions);
        transactions.setUser(this);
        return this;
    }

    public Users removeTransactions(Transactions transactions) {
        this.transactions.remove(transactions);
        transactions.setUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return getId() != null && getId().equals(((Users) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", walletAmount=" + getWalletAmount() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", emailVerifiedAt='" + getEmailVerifiedAt() + "'" +
            ", password='" + getPassword() + "'" +
            ", rememberToken='" + getRememberToken() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", name='" + getName() + "'" +
            ", fcm='" + getFcm() + "'" +
            ", subscriptionAmount=" + getSubscriptionAmount() +
            "}";
    }
}

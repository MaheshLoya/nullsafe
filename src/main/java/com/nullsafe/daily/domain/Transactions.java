package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Transactions.
 */
@Entity
@Table(name = "transactions")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 65535)
    @Column(name = "payment_id", length = 65535)
    private String paymentId;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    /**
     * 1&#61;credit 2&#61;debited
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 1&#61;online,2&#61;cash
     */
    @NotNull
    @Column(name = "payment_mode", nullable = false)
    private Integer paymentMode;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "trasation", "product", "address", "orderUserAssigns", "subscribedOrderDeliveries", "transactions" },
        allowSetters = true
    )
    private Orders order;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trasation")
    @JsonIgnoreProperties(
        value = { "user", "trasation", "product", "address", "orderUserAssigns", "subscribedOrderDeliveries", "transactions" },
        allowSetters = true
    )
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @JsonIgnoreProperties(value = { "user", "transaction", "product", "address" }, allowSetters = true)
    private Set<SubscribedOrders> subscribedOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transactions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    public Transactions paymentId(String paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Transactions amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public Transactions description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return this.type;
    }

    public Transactions type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPaymentMode() {
        return this.paymentMode;
    }

    public Transactions paymentMode(Integer paymentMode) {
        this.setPaymentMode(paymentMode);
        return this;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Transactions createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Transactions updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Orders getOrder() {
        return this.order;
    }

    public void setOrder(Orders orders) {
        this.order = orders;
    }

    public Transactions order(Orders orders) {
        this.setOrder(orders);
        return this;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    public Transactions user(Users users) {
        this.setUser(users);
        return this;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setTrasation(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setTrasation(this));
        }
        this.orders = orders;
    }

    public Transactions orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public Transactions addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setTrasation(this);
        return this;
    }

    public Transactions removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setTrasation(null);
        return this;
    }

    public Set<SubscribedOrders> getSubscribedOrders() {
        return this.subscribedOrders;
    }

    public void setSubscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        if (this.subscribedOrders != null) {
            this.subscribedOrders.forEach(i -> i.setTransaction(null));
        }
        if (subscribedOrders != null) {
            subscribedOrders.forEach(i -> i.setTransaction(this));
        }
        this.subscribedOrders = subscribedOrders;
    }

    public Transactions subscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        this.setSubscribedOrders(subscribedOrders);
        return this;
    }

    public Transactions addSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.add(subscribedOrders);
        subscribedOrders.setTransaction(this);
        return this;
    }

    public Transactions removeSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.remove(subscribedOrders);
        subscribedOrders.setTransaction(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transactions)) {
            return false;
        }
        return getId() != null && getId().equals(((Transactions) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transactions{" +
            "id=" + getId() +
            ", paymentId='" + getPaymentId() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", type=" + getType() +
            ", paymentMode=" + getPaymentMode() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

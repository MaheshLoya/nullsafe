package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A SubscribedOrderDelivery.
 */
@Entity
@Table(name = "subscribed_order_delivery")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrderDelivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    /**
     * 1&#61; online, 2&#61;offline
     */
    @Column(name = "payment_mode")
    private Integer paymentMode;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
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
    private Users entryUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscribedOrderDelivery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public SubscribedOrderDelivery date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPaymentMode() {
        return this.paymentMode;
    }

    public SubscribedOrderDelivery paymentMode(Integer paymentMode) {
        this.setPaymentMode(paymentMode);
        return this;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SubscribedOrderDelivery createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SubscribedOrderDelivery updatedAt(Instant updatedAt) {
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

    public SubscribedOrderDelivery order(Orders orders) {
        this.setOrder(orders);
        return this;
    }

    public Users getEntryUser() {
        return this.entryUser;
    }

    public void setEntryUser(Users users) {
        this.entryUser = users;
    }

    public SubscribedOrderDelivery entryUser(Users users) {
        this.setEntryUser(users);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribedOrderDelivery)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscribedOrderDelivery) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrderDelivery{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", paymentMode=" + getPaymentMode() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

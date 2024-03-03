package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.SubscribedOrderDelivery} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrderDeliveryDTO implements Serializable {

    private Long id;

    private LocalDate date;

    /**
     * 1&#61; online, 2&#61;offline
     */
    @Schema(description = "1&#61; online, 2&#61;offline")
    private Integer paymentMode;

    private Instant createdAt;

    private Instant updatedAt;

    private OrdersDTO order;

    private UsersDTO entryUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OrdersDTO getOrder() {
        return order;
    }

    public void setOrder(OrdersDTO order) {
        this.order = order;
    }

    public UsersDTO getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(UsersDTO entryUser) {
        this.entryUser = entryUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscribedOrderDeliveryDTO)) {
            return false;
        }

        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = (SubscribedOrderDeliveryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscribedOrderDeliveryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrderDeliveryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", paymentMode=" + getPaymentMode() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", order=" + getOrder() +
            ", entryUser=" + getEntryUser() +
            "}";
    }
}

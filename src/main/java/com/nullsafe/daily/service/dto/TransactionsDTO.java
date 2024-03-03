package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Transactions} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionsDTO implements Serializable {

    private Long id;

    @Size(max = 65535)
    private String paymentId;

    @NotNull
    private Double amount;

    @Size(max = 65535)
    private String description;

    /**
     * 1&#61;credit 2&#61;debited
     */
    @Schema(description = "1&#61;credit 2&#61;debited")
    private Integer type;

    /**
     * 1&#61;online,2&#61;cash
     */
    @NotNull
    @Schema(description = "1&#61;online,2&#61;cash", required = true)
    private Integer paymentMode;

    private Instant createdAt;

    private Instant updatedAt;

    private OrdersDTO order;

    private UsersDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionsDTO)) {
            return false;
        }

        TransactionsDTO transactionsDTO = (TransactionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionsDTO{" +
            "id=" + getId() +
            ", paymentId='" + getPaymentId() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", type=" + getType() +
            ", paymentMode=" + getPaymentMode() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", order=" + getOrder() +
            ", user=" + getUser() +
            "}";
    }
}

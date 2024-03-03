package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Orders} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersDTO implements Serializable {

    private Long id;

    /**
     * 1&#61; prepaid,2&#61;pos, 3&#61;pay now,4&#61;cod&#13;&#10;
     */
    @Schema(description = "1&#61; prepaid,2&#61;pos, 3&#61;pay now,4&#61;cod&#13;&#10;")
    private Integer orderType;

    @NotNull
    private Double orderAmount;

    @NotNull
    private Double price;

    @NotNull
    private Double mrp;

    @NotNull
    private Double tax;

    private Integer qty;

    @Size(max = 65535)
    private String selectedDaysForWeekly;

    private LocalDate startDate;

    /**
     * 1&#61;daliy,2&#61;weekly,3&#61;monthly,4&#61;alternative days&#13;&#10;
     */
    @Schema(description = "1&#61;daliy,2&#61;weekly,3&#61;monthly,4&#61;alternative days&#13;&#10;")
    private Integer subscriptionType;

    /**
     * 1&#61;confirmed 1&#61;confirmed, 0&#61;pending,2&#61;canceled
     */
    @NotNull
    @Schema(description = "1&#61;confirmed 1&#61;confirmed, 0&#61;pending,2&#61;canceled", required = true)
    private Integer status;

    /**
     * 1&#61;delivered
     */
    @Schema(description = "1&#61;delivered")
    private Integer deliveryStatus;

    /**
     * 0&#61;active,1&#61;stop
     */
    @NotNull
    @Schema(description = "0&#61;active,1&#61;stop", required = true)
    private Boolean orderStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private UsersDTO user;

    private TransactionsDTO trasation;

    private ProductDTO product;

    private UserAddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getSelectedDaysForWeekly() {
        return selectedDaysForWeekly;
    }

    public void setSelectedDaysForWeekly(String selectedDaysForWeekly) {
        this.selectedDaysForWeekly = selectedDaysForWeekly;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Boolean getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
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

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }

    public TransactionsDTO getTrasation() {
        return trasation;
    }

    public void setTrasation(TransactionsDTO trasation) {
        this.trasation = trasation;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public UserAddressDTO getAddress() {
        return address;
    }

    public void setAddress(UserAddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdersDTO)) {
            return false;
        }

        OrdersDTO ordersDTO = (OrdersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersDTO{" +
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
            ", user=" + getUser() +
            ", trasation=" + getTrasation() +
            ", product=" + getProduct() +
            ", address=" + getAddress() +
            "}";
    }
}

package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.SubscribedOrders} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscribedOrdersDTO implements Serializable {

    private Long id;

    /**
     * 1&#61; prepaid-online,2&#61;prepaid-cash, 3&#61;postpaid-online,4&#61;postpaid-cash
     */
    @Schema(description = "1&#61; prepaid-online,2&#61;prepaid-cash, 3&#61;postpaid-online,4&#61;postpaid-cash")
    private Integer paymentType;

    @NotNull
    private Float orderAmount;

    private Float subscriptionBalanceAmount;

    @NotNull
    private Float price;

    @NotNull
    private Float mrp;

    @NotNull
    private Float tax;

    private Integer qty;

    private Integer offerId;

    @Size(max = 65535)
    private String selectedDaysForWeekly;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private LocalDate lastRenewalDate;

    /**
     * 1&#61;daily,2&#61;weekly,3&#61;monthly,4&#61;alternative days
     */
    @Schema(description = "1&#61;daily,2&#61;weekly,3&#61;monthly,4&#61;alternative days")
    private Integer subscriptionType;

    /**
     * 0&#61;pending, 1&#61;confirmed, 2&#61;canceled
     */
    @NotNull
    @Schema(description = "0&#61;pending, 1&#61;confirmed, 2&#61;canceled", required = true)
    private Integer approvalStatus;

    /**
     * 0&#61;active,1&#61;stop
     */
    @NotNull
    @Schema(description = "0&#61;active,1&#61;stop", required = true)
    private Boolean orderStatus;

    private Instant createdAt;

    private Instant updatedAt;

    @Size(max = 255)
    private String createdBy;

    @Size(max = 255)
    private String updatedBy;

    private UsersDTO user;

    private TransactionsDTO transaction;

    private ProductDTO product;

    private UserAddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Float getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Float orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Float getSubscriptionBalanceAmount() {
        return subscriptionBalanceAmount;
    }

    public void setSubscriptionBalanceAmount(Float subscriptionBalanceAmount) {
        this.subscriptionBalanceAmount = subscriptionBalanceAmount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getMrp() {
        return mrp;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getLastRenewalDate() {
        return lastRenewalDate;
    }

    public void setLastRenewalDate(LocalDate lastRenewalDate) {
        this.lastRenewalDate = lastRenewalDate;
    }

    public Integer getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }

    public TransactionsDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionsDTO transaction) {
        this.transaction = transaction;
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
        if (!(o instanceof SubscribedOrdersDTO)) {
            return false;
        }

        SubscribedOrdersDTO subscribedOrdersDTO = (SubscribedOrdersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscribedOrdersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscribedOrdersDTO{" +
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
            ", user=" + getUser() +
            ", transaction=" + getTransaction() +
            ", product=" + getProduct() +
            ", address=" + getAddress() +
            "}";
    }
}

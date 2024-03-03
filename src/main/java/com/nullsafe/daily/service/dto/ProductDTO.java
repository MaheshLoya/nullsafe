package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    private String title;

    @NotNull
    @Size(max = 250)
    private String qtyText;

    private Long stockQty;

    @NotNull
    private Double price;

    @NotNull
    private Double tax;

    @NotNull
    private Double mrp;

    @Size(max = 250)
    private String offerText;

    @Size(max = 65535)
    private String description;

    @Size(max = 65535)
    private String disclaimer;

    /**
     * 1&#61; true ,0&#61;false
     */
    @NotNull
    @Schema(description = "1&#61; true ,0&#61;false", required = true)
    private Boolean subscription;

    private Instant createdAt;

    private Instant updatedAt;

    /**
     * 0 is Inactive, 1 is Active
     */
    @NotNull
    @Schema(description = "0 is Inactive, 1 is Active", required = true)
    private Boolean isActive;

    private SubCatDTO subCat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQtyText() {
        return qtyText;
    }

    public void setQtyText(String qtyText) {
        this.qtyText = qtyText;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Boolean getSubscription() {
        return subscription;
    }

    public void setSubscription(Boolean subscription) {
        this.subscription = subscription;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SubCatDTO getSubCat() {
        return subCat;
    }

    public void setSubCat(SubCatDTO subCat) {
        this.subCat = subCat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", qtyText='" + getQtyText() + "'" +
            ", stockQty=" + getStockQty() +
            ", price=" + getPrice() +
            ", tax=" + getTax() +
            ", mrp=" + getMrp() +
            ", offerText='" + getOfferText() + "'" +
            ", description='" + getDescription() + "'" +
            ", disclaimer='" + getDisclaimer() + "'" +
            ", subscription='" + getSubscription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", subCat=" + getSubCat() +
            "}";
    }
}

package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Product} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter qtyText;

    private LongFilter stockQty;

    private DoubleFilter price;

    private DoubleFilter tax;

    private DoubleFilter mrp;

    private StringFilter offerText;

    private StringFilter description;

    private StringFilter disclaimer;

    private BooleanFilter subscription;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private BooleanFilter isActive;

    private LongFilter subCatId;

    private LongFilter cartId;

    private LongFilter ordersId;

    private LongFilter subscribedOrdersId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.qtyText = other.qtyText == null ? null : other.qtyText.copy();
        this.stockQty = other.stockQty == null ? null : other.stockQty.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.mrp = other.mrp == null ? null : other.mrp.copy();
        this.offerText = other.offerText == null ? null : other.offerText.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.disclaimer = other.disclaimer == null ? null : other.disclaimer.copy();
        this.subscription = other.subscription == null ? null : other.subscription.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.subCatId = other.subCatId == null ? null : other.subCatId.copy();
        this.cartId = other.cartId == null ? null : other.cartId.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.subscribedOrdersId = other.subscribedOrdersId == null ? null : other.subscribedOrdersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getQtyText() {
        return qtyText;
    }

    public StringFilter qtyText() {
        if (qtyText == null) {
            qtyText = new StringFilter();
        }
        return qtyText;
    }

    public void setQtyText(StringFilter qtyText) {
        this.qtyText = qtyText;
    }

    public LongFilter getStockQty() {
        return stockQty;
    }

    public LongFilter stockQty() {
        if (stockQty == null) {
            stockQty = new LongFilter();
        }
        return stockQty;
    }

    public void setStockQty(LongFilter stockQty) {
        this.stockQty = stockQty;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
        this.tax = tax;
    }

    public DoubleFilter getMrp() {
        return mrp;
    }

    public DoubleFilter mrp() {
        if (mrp == null) {
            mrp = new DoubleFilter();
        }
        return mrp;
    }

    public void setMrp(DoubleFilter mrp) {
        this.mrp = mrp;
    }

    public StringFilter getOfferText() {
        return offerText;
    }

    public StringFilter offerText() {
        if (offerText == null) {
            offerText = new StringFilter();
        }
        return offerText;
    }

    public void setOfferText(StringFilter offerText) {
        this.offerText = offerText;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getDisclaimer() {
        return disclaimer;
    }

    public StringFilter disclaimer() {
        if (disclaimer == null) {
            disclaimer = new StringFilter();
        }
        return disclaimer;
    }

    public void setDisclaimer(StringFilter disclaimer) {
        this.disclaimer = disclaimer;
    }

    public BooleanFilter getSubscription() {
        return subscription;
    }

    public BooleanFilter subscription() {
        if (subscription == null) {
            subscription = new BooleanFilter();
        }
        return subscription;
    }

    public void setSubscription(BooleanFilter subscription) {
        this.subscription = subscription;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getSubCatId() {
        return subCatId;
    }

    public LongFilter subCatId() {
        if (subCatId == null) {
            subCatId = new LongFilter();
        }
        return subCatId;
    }

    public void setSubCatId(LongFilter subCatId) {
        this.subCatId = subCatId;
    }

    public LongFilter getCartId() {
        return cartId;
    }

    public LongFilter cartId() {
        if (cartId == null) {
            cartId = new LongFilter();
        }
        return cartId;
    }

    public void setCartId(LongFilter cartId) {
        this.cartId = cartId;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            ordersId = new LongFilter();
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getSubscribedOrdersId() {
        return subscribedOrdersId;
    }

    public LongFilter subscribedOrdersId() {
        if (subscribedOrdersId == null) {
            subscribedOrdersId = new LongFilter();
        }
        return subscribedOrdersId;
    }

    public void setSubscribedOrdersId(LongFilter subscribedOrdersId) {
        this.subscribedOrdersId = subscribedOrdersId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(qtyText, that.qtyText) &&
            Objects.equals(stockQty, that.stockQty) &&
            Objects.equals(price, that.price) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(mrp, that.mrp) &&
            Objects.equals(offerText, that.offerText) &&
            Objects.equals(description, that.description) &&
            Objects.equals(disclaimer, that.disclaimer) &&
            Objects.equals(subscription, that.subscription) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(subCatId, that.subCatId) &&
            Objects.equals(cartId, that.cartId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(subscribedOrdersId, that.subscribedOrdersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            qtyText,
            stockQty,
            price,
            tax,
            mrp,
            offerText,
            description,
            disclaimer,
            subscription,
            createdAt,
            updatedAt,
            isActive,
            subCatId,
            cartId,
            ordersId,
            subscribedOrdersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (qtyText != null ? "qtyText=" + qtyText + ", " : "") +
            (stockQty != null ? "stockQty=" + stockQty + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (mrp != null ? "mrp=" + mrp + ", " : "") +
            (offerText != null ? "offerText=" + offerText + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (disclaimer != null ? "disclaimer=" + disclaimer + ", " : "") +
            (subscription != null ? "subscription=" + subscription + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (subCatId != null ? "subCatId=" + subCatId + ", " : "") +
            (cartId != null ? "cartId=" + cartId + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (subscribedOrdersId != null ? "subscribedOrdersId=" + subscribedOrdersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

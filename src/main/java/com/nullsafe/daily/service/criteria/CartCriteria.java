package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Cart} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.CartResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter qty;

    private DoubleFilter price;

    private DoubleFilter totalPrice;

    private DoubleFilter mrp;

    private DoubleFilter tax;

    private StringFilter qtyText;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter productId;

    private LongFilter userId;

    private Boolean distinct;

    public CartCriteria() {}

    public CartCriteria(CartCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.mrp = other.mrp == null ? null : other.mrp.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.qtyText = other.qtyText == null ? null : other.qtyText.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CartCriteria copy() {
        return new CartCriteria(this);
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

    public IntegerFilter getQty() {
        return qty;
    }

    public IntegerFilter qty() {
        if (qty == null) {
            qty = new IntegerFilter();
        }
        return qty;
    }

    public void setQty(IntegerFilter qty) {
        this.qty = qty;
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

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
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

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final CartCriteria that = (CartCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(price, that.price) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(mrp, that.mrp) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(qtyText, that.qtyText) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qty, price, totalPrice, mrp, tax, qtyText, createdAt, updatedAt, productId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (mrp != null ? "mrp=" + mrp + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (qtyText != null ? "qtyText=" + qtyText + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

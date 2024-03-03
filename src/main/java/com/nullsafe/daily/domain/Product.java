package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @NotNull
    @Size(max = 250)
    @Column(name = "qty_text", length = 250, nullable = false)
    private String qtyText;

    @Column(name = "stock_qty")
    private Long stockQty;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Double tax;

    @NotNull
    @Column(name = "mrp", nullable = false)
    private Double mrp;

    @Size(max = 250)
    @Column(name = "offer_text", length = 250)
    private String offerText;

    @Size(max = 65535)
    @Column(name = "description", length = 65535)
    private String description;

    @Size(max = 65535)
    @Column(name = "disclaimer", length = 65535)
    private String disclaimer;

    /**
     * 1&#61; true ,0&#61;false
     */
    @NotNull
    @Column(name = "subscription", nullable = false)
    private Boolean subscription;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 0 is Inactive, 1 is Active
     */
    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cat", "products" }, allowSetters = true)
    private SubCat subCat;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "product", "user" }, allowSetters = true)
    private Set<Cart> carts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(
        value = { "user", "trasation", "product", "address", "orderUserAssigns", "subscribedOrderDeliveries", "transactions" },
        allowSetters = true
    )
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "user", "transaction", "product", "address" }, allowSetters = true)
    private Set<SubscribedOrders> subscribedOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Product title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQtyText() {
        return this.qtyText;
    }

    public Product qtyText(String qtyText) {
        this.setQtyText(qtyText);
        return this;
    }

    public void setQtyText(String qtyText) {
        this.qtyText = qtyText;
    }

    public Long getStockQty() {
        return this.stockQty;
    }

    public Product stockQty(Long stockQty) {
        this.setStockQty(stockQty);
        return this;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public Double getPrice() {
        return this.price;
    }

    public Product price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return this.tax;
    }

    public Product tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getMrp() {
        return this.mrp;
    }

    public Product mrp(Double mrp) {
        this.setMrp(mrp);
        return this;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getOfferText() {
        return this.offerText;
    }

    public Product offerText(String offerText) {
        this.setOfferText(offerText);
        return this;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisclaimer() {
        return this.disclaimer;
    }

    public Product disclaimer(String disclaimer) {
        this.setDisclaimer(disclaimer);
        return this;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Boolean getSubscription() {
        return this.subscription;
    }

    public Product subscription(Boolean subscription) {
        this.setSubscription(subscription);
        return this;
    }

    public void setSubscription(Boolean subscription) {
        this.subscription = subscription;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Product isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SubCat getSubCat() {
        return this.subCat;
    }

    public void setSubCat(SubCat subCat) {
        this.subCat = subCat;
    }

    public Product subCat(SubCat subCat) {
        this.setSubCat(subCat);
        return this;
    }

    public Set<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(Set<Cart> carts) {
        if (this.carts != null) {
            this.carts.forEach(i -> i.setProduct(null));
        }
        if (carts != null) {
            carts.forEach(i -> i.setProduct(this));
        }
        this.carts = carts;
    }

    public Product carts(Set<Cart> carts) {
        this.setCarts(carts);
        return this;
    }

    public Product addCart(Cart cart) {
        this.carts.add(cart);
        cart.setProduct(this);
        return this;
    }

    public Product removeCart(Cart cart) {
        this.carts.remove(cart);
        cart.setProduct(null);
        return this;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setProduct(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setProduct(this));
        }
        this.orders = orders;
    }

    public Product orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public Product addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setProduct(this);
        return this;
    }

    public Product removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setProduct(null);
        return this;
    }

    public Set<SubscribedOrders> getSubscribedOrders() {
        return this.subscribedOrders;
    }

    public void setSubscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        if (this.subscribedOrders != null) {
            this.subscribedOrders.forEach(i -> i.setProduct(null));
        }
        if (subscribedOrders != null) {
            subscribedOrders.forEach(i -> i.setProduct(this));
        }
        this.subscribedOrders = subscribedOrders;
    }

    public Product subscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        this.setSubscribedOrders(subscribedOrders);
        return this;
    }

    public Product addSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.add(subscribedOrders);
        subscribedOrders.setProduct(this);
        return this;
    }

    public Product removeSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.remove(subscribedOrders);
        subscribedOrders.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
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
            "}";
    }
}

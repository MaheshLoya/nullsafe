package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A SubCat.
 */
@Entity
@Table(name = "sub_cat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubCat implements Serializable {

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
    @JsonIgnoreProperties(value = { "subCats" }, allowSetters = true)
    private Cat cat;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subCat")
    @JsonIgnoreProperties(value = { "subCat", "carts", "orders", "subscribedOrders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubCat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SubCat title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SubCat createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SubCat updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SubCat isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Cat getCat() {
        return this.cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public SubCat cat(Cat cat) {
        this.setCat(cat);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setSubCat(null));
        }
        if (products != null) {
            products.forEach(i -> i.setSubCat(this));
        }
        this.products = products;
    }

    public SubCat products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public SubCat addProduct(Product product) {
        this.products.add(product);
        product.setSubCat(this);
        return this;
    }

    public SubCat removeProduct(Product product) {
        this.products.remove(product);
        product.setSubCat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCat)) {
            return false;
        }
        return getId() != null && getId().equals(((SubCat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCat{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
